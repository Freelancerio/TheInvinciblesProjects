#!/bin/bash

# Coverage Report Merger
# This script merges backend (JaCoCo) and frontend (Jest) coverage reports
# Usage: ./merge-coverage.sh

set -e

echo "📊 Starting coverage report merge..."

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Define paths
BACKEND_COVERAGE="Backend/target/site/jacoco/jacoco.xml"
FRONTEND_COVERAGE="frontend/coverage/lcov.info"
COVERAGE_DIR="./coverage-reports"
MERGED_REPORT="$COVERAGE_DIR/merged-coverage.xml"

# Create coverage directory
mkdir -p "$COVERAGE_DIR"

# Check if backend coverage exists
if [ ! -f "$BACKEND_COVERAGE" ]; then
    echo -e "${RED}❌ Backend coverage file not found: $BACKEND_COVERAGE${NC}"
    echo "Run: cd Backend && mvn clean verify"
    exit 1
fi

echo -e "${GREEN}✅ Backend coverage found${NC}"
cp "$BACKEND_COVERAGE" "$COVERAGE_DIR/backend-jacoco.xml"

# Check if frontend coverage exists
if [ ! -f "$FRONTEND_COVERAGE" ]; then
    echo -e "${RED}❌ Frontend coverage file not found: $FRONTEND_COVERAGE${NC}"
    echo "Run: cd frontend && npm run test:ci"
    exit 1
fi

echo -e "${GREEN}✅ Frontend coverage found${NC}"
cp "$FRONTEND_COVERAGE" "$COVERAGE_DIR/frontend-lcov.info"

# Extract coverage percentages
echo ""
echo "📈 Coverage Summary:"
echo "===================="

# Backend coverage from JaCoCo
BACKEND_PERCENTAGE=$(grep -oP 'line-rate="\K[^"]+' "$COVERAGE_DIR/backend-jacoco.xml" | head -1)
if [ ! -z "$BACKEND_PERCENTAGE" ]; then
    BACKEND_COVERAGE_PCT=$(echo "scale=2; $BACKEND_PERCENTAGE * 100" | bc)
    echo -e "Backend (Java):   ${GREEN}${BACKEND_COVERAGE_PCT}%${NC}"
else
    echo -e "Backend (Java):   ${YELLOW}Unable to parse${NC}"
fi

# Frontend coverage from LCOV
FRONTEND_PERCENTAGE=$(grep "^LH:" "$COVERAGE_DIR/frontend-lcov.info" | head -1 | cut -d':' -f2)
FRONTEND_TOTAL=$(grep "^LF:" "$COVERAGE_DIR/frontend-lcov.info" | head -1 | cut -d':' -f2)

if [ ! -z "$FRONTEND_PERCENTAGE" ] && [ ! -z "$FRONTEND_TOTAL" ]; then
    FRONTEND_COVERAGE_PCT=$(echo "scale=2; ($FRONTEND_PERCENTAGE / $FRONTEND_TOTAL) * 100" | bc)
    echo -e "Frontend (React): ${GREEN}${FRONTEND_COVERAGE_PCT}%${NC}"
else
    echo -e "Frontend (React): ${YELLOW}Unable to parse${NC}"
fi

# Calculate combined average
if [ ! -z "$BACKEND_COVERAGE_PCT" ] && [ ! -z "$FRONTEND_COVERAGE_PCT" ]; then
    COMBINED_COVERAGE=$(echo "scale=2; ($BACKEND_COVERAGE_PCT + $FRONTEND_COVERAGE_PCT) / 2" | bc)
    echo -e "Combined Average: ${GREEN}${COMBINED_COVERAGE}%${NC}"
fi

echo ""
echo "📁 Coverage files saved to: $COVERAGE_DIR"
echo "  - backend-jacoco.xml"
echo "  - frontend-lcov.info"
echo ""
echo -e "${GREEN}✅ Coverage merge complete!${NC}"
echo ""
echo "Ready to upload to Codecov with:"
echo "codecov -f ./Backend/target/site/jacoco/jacoco.xml"
echo "codecov -f ./frontend/coverage/lcov.info"
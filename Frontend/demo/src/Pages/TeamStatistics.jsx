import React, { useState, useEffect } from 'react';

const TeamStatisticsPage = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [teamSearchTerm, setTeamSearchTerm] = useState('');
  const [selectedTeamId, setSelectedTeamId] = useState(134491); // Default team ID
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  
  // State for all data
  const [teamData, setTeamData] = useState(null);
  const [seasonStats, setSeasonStats] = useState([]);
  const [homeAwayStats, setHomeAwayStats] = useState({ homeWins: 0, awayWins: 0 });
  const [fixtures, setFixtures] = useState([]);
  const [matchStats, setMatchStats] = useState(null);
  const [availableTeams, setAvailableTeams] = useState([]);

  const API_BASE_URL = 'http://localhost:8080/api/database/psl';

  useEffect(() => {
    fetchAllTeams();
  }, []);

  useEffect(() => {
    if (selectedTeamId) {
      fetchTeamData(selectedTeamId);
    }
  }, [selectedTeamId]);

  const fetchAllTeams = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/teams`);
      if (response.ok) {
        const data = await response.json();
        setAvailableTeams(data.teams || []);
      }
    } catch (error) {
      console.error('Error fetching teams:', error);
      setError('Failed to load teams. Please check your connection.');
    }
  };

  const fetchTeamData = async (teamId) => {
    if (!teamId) return;
    
    setLoading(true);
    setError(null);
    
    try {
      // Fetch team basic info
      const teamResponse = await fetch(`${API_BASE_URL}/teams/${teamId}`);
      if (teamResponse.ok) {
        const teamInfo = await teamResponse.json();
        setTeamData(teamInfo.team);
      }

      // Fetch season statistics
      const seasonStatsResponse = await fetch(`${API_BASE_URL}/teams/${teamId}/season-stats`);
      if (seasonStatsResponse.ok) {
        const seasonData = await seasonStatsResponse.json();
        const stats = seasonData.statistics;
        
        // Transform the data into the format expected by the component
        const formattedSeasonStats = [
          { statistic: "Matches Played", value: stats?.matchesPlayed || "0" },
          { statistic: "Wins", value: stats?.wins || "0" },
          { statistic: "Draws", value: stats?.draws || "0" },
          { statistic: "Losses", value: stats?.losses || "0" },
          { statistic: "Goals For", value: stats?.goalsFor || "0" },
          { statistic: "Goals Against", value: stats?.goalsAgainst || "0" },
          { statistic: "Goal Difference", value: stats?.goalDifference > 0 ? `+${stats.goalDifference}` : stats?.goalDifference?.toString() || "0" },
          { statistic: "Points", value: stats?.points || "0" }
        ];
        setSeasonStats(formattedSeasonStats);
      }

      // Fetch home/away statistics
      const homeAwayResponse = await fetch(`${API_BASE_URL}/teams/${teamId}/home-away-stats`);
      if (homeAwayResponse.ok) {
        const homeAwayData = await homeAwayResponse.json();
        setHomeAwayStats({
          homeWins: homeAwayData.homeWins || 0,
          awayWins: homeAwayData.awayWins || 0
        });
      }

      // Fetch upcoming fixtures
      const fixturesResponse = await fetch(`${API_BASE_URL}/teams/${teamId}/upcoming-fixtures?limit=5`);
      if (fixturesResponse.ok) {
        const fixturesData = await fixturesResponse.json();
        const formattedFixtures = (fixturesData.fixtures || []).map(fixture => ({
          opponent: fixture.opponentName,
          dateTime: new Date(fixture.matchDatetime).toLocaleString(),
          venue: fixture.venue,
          homeAway: fixture.isHome ? "Home" : "Away"
        }));
        setFixtures(formattedFixtures);
      }

      // Fetch match statistics
      const matchStatsResponse = await fetch(`${API_BASE_URL}/teams/${teamId}/match-stats`);
      if (matchStatsResponse.ok) {
        const matchStatsData = await matchStatsResponse.json();
        setMatchStats(matchStatsData.statistics);
      }

    } catch (error) {
      console.error('Error fetching team data:', error);
      setError('Failed to load team data. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleTeamSearch = async () => {
    if (!teamSearchTerm.trim()) return;
    
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/teams/search?name=${encodeURIComponent(teamSearchTerm)}`);
      if (response.ok) {
        const data = await response.json();
        if (data.teams && data.teams.length > 0) {
          setSelectedTeamId(data.teams[0].teamId);
          setTeamSearchTerm('');
          setError(null);
        } else {
          setError('No team found with that name.');
        }
      } else {
        setError('Search failed. Please try again.');
      }
    } catch (error) {
      console.error('Error searching for team:', error);
      setError('Error searching for team.');
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleTeamSearch();
    }
  };

  if (loading && !teamData && availableTeams.length === 0) {
    return (
      <div style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh'
      }}>
        <div style={{ fontSize: '18px' }}>Loading team statistics...</div>
      </div>
    );
  }

  return (
    <div style={{
      position: 'relative',
      display: 'flex',
      width: '100%',
      height: '100%',
      minHeight: '100vh',
      flexDirection: 'column',
      backgroundColor: '#f5f5f5',
      overflowX: 'hidden',
      fontFamily: '"Public Sans", "Noto Sans", sans-serif'
    }}>
      <div style={{
        display: 'flex',
        height: '100%',
        flexGrow: 1,
        flexDirection: 'column'
      }}>
        {/* Header */}
        <header style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          whiteSpace: 'nowrap',
          borderBottom: '1px solid #ededed',
          paddingLeft: '40px',
          paddingRight: '40px',
          paddingTop: '12px',
          paddingBottom: '12px'
        }}>
          <div style={{
            display: 'flex',
            alignItems: 'center',
            gap: '32px'
          }}>
            <div style={{
              display: 'flex',
              alignItems: 'center',
              gap: '16px',
              color: '#141414'
            }}>
              <div style={{ width: '16px', height: '16px' }}>
                <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M4 42.4379C4 42.4379 14.0962 36.0744 24 41.1692C35.0664 46.8624 44 42.2078 44 42.2078L44 7.01134C44 7.01134 35.068 11.6577 24.0031 5.96913C14.0971 0.876274 4 7.27094 4 7.27094L4 42.4379Z"
                    fill="currentColor"
                  />
                </svg>
              </div>
              <h2 style={{
                color: '#141414',
                fontSize: '18px',
                fontWeight: 'bold',
                lineHeight: 'tight',
                letterSpacing: '-0.015em',
                margin: 0
              }}>SmartBet</h2>
            </div>
            <div style={{
              display: 'flex',
              alignItems: 'center',
              gap: '36px'
            }}>
              <a style={{
                color: '#141414',
                fontSize: '14px',
                fontWeight: '500',
                lineHeight: 'normal',
                textDecoration: 'none'
              }} href="#">Home</a>
              <a style={{
                color: '#141414',
                fontSize: '14px',
                fontWeight: '500',
                lineHeight: 'normal',
                textDecoration: 'none'
              }} href="#">Teams</a>
              <a style={{
                color: '#141414',
                fontSize: '14px',
                fontWeight: '500',
                lineHeight: 'normal',
                textDecoration: 'none'
              }} href="#">Fixtures</a>
              <a style={{
                color: '#141414',
                fontSize: '14px',
                fontWeight: '500',
                lineHeight: 'normal',
                textDecoration: 'none'
              }} href="#">Standings</a>
              <a style={{
                color: '#141414',
                fontSize: '14px',
                fontWeight: '500',
                lineHeight: 'normal',
                textDecoration: 'none'
              }} href="#">News</a>
            </div>
          </div>
          <div style={{
            display: 'flex',
            flexGrow: 1,
            justifyContent: 'flex-end',
            gap: '32px'
          }}>
            <label style={{
              display: 'flex',
              flexDirection: 'column',
              minWidth: '160px',
              height: '40px',
              maxWidth: '256px'
            }}>
              <div style={{
                display: 'flex',
                width: '100%',
                flex: '1 1 0%',
                alignItems: 'stretch',
                borderRadius: '8px',
                height: '100%'
              }}>
                <div style={{
                  color: '#737373',
                  display: 'flex',
                  border: 'none',
                  backgroundColor: '#ededed',
                  alignItems: 'center',
                  justifyContent: 'center',
                  paddingLeft: '16px',
                  borderTopLeftRadius: '8px',
                  borderBottomLeftRadius: '8px',
                  borderRight: '0'
                }}>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor" viewBox="0 0 256 256">
                    <path d="M229.66,218.34l-50.07-50.06a88.11,88.11,0,1,0-11.31,11.31l50.06,50.07a8,8,0,0,0,11.32-11.32ZM40,112a72,72,0,1,1,72,72A72.08,72.08,0,0,1,40,112Z" />
                  </svg>
                </div>
                <input
                  placeholder="Search for a team"
                  style={{
                    display: 'flex',
                    width: '100%',
                    minWidth: '0',
                    flex: '1 1 0%',
                    resize: 'none',
                    overflow: 'hidden',
                    borderRadius: '8px',
                    color: '#141414',
                    outline: '0',
                    border: 'none',
                    backgroundColor: '#ededed',
                    height: '100%',
                    paddingLeft: '8px',
                    paddingRight: '16px',
                    borderTopLeftRadius: '0',
                    borderBottomLeftRadius: '0',
                    borderLeft: '0',
                    fontSize: '16px',
                    fontWeight: 'normal',
                    lineHeight: 'normal'
                  }}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
            </label>
            <button style={{
              display: 'flex',
              maxWidth: '480px',
              cursor: 'pointer',
              alignItems: 'center',
              justifyContent: 'center',
              overflow: 'hidden',
              borderRadius: '8px',
              height: '40px',
              backgroundColor: '#ededed',
              color: '#141414',
              gap: '8px',
              fontSize: '14px',
              fontWeight: 'bold',
              lineHeight: 'normal',
              letterSpacing: '0.015em',
              minWidth: '0',
              paddingLeft: '10px',
              paddingRight: '10px',
              border: 'none'
            }}>
              <div style={{ color: '#141414' }}>
                <svg xmlns="http://www.w3.org/2000/svg" width="20px" height="20px" fill="currentColor" viewBox="0 0 256 256">
                  <path d="M221.8,175.94C216.25,166.38,208,139.33,208,104a80,80,0,1,0-160,0c0,35.34-8.26,62.38-13.81,71.94A16,16,0,0,0,48,200H88.81a40,40,0,0,0,78.38,0H208a16,16,0,0,0,13.8-24.06ZM128,216a24,24,0,0,1-22.62-16h45.24A24,24,0,0,1,128,216ZM48,184c7.7-13.24,16-43.92,16-80a64,64,0,1,1,128,0c0,36.05,8.28,66.73,16,80Z" />
                </svg>
              </div>
            </button>
            <div style={{
              backgroundImage: '',
              backgroundSize: 'cover',
              backgroundPosition: 'center',
              backgroundRepeat: 'no-repeat',
              aspectRatio: '1',
              borderRadius: '50%',
              width: '40px',
              height: '40px'
            }} />
          </div>
        </header>

        {/* Main Content */}
        <div style={{
          paddingLeft: '160px',
          paddingRight: '160px',
          display: 'flex',
          flex: '1 1 0%',
          justifyContent: 'center',
          paddingTop: '20px',
          paddingBottom: '20px'
        }}>
          <div style={{
            display: 'flex',
            flexDirection: 'column',
            maxWidth: '960px',
            flex: '1 1 0%'
          }}>
            {/* Error Display */}
            {error && (
              <div style={{
                margin: '16px',
                marginBottom: '16px',
                padding: '16px',
                backgroundColor: '#fef2f2',
                border: '1px solid #fca5a5',
                color: '#dc2626',
                borderRadius: '4px',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center'
              }}>
                <span>{error}</span>
                <button 
                  onClick={() => setError(null)}
                  style={{
                    color: '#dc2626',
                    marginLeft: '16px',
                    background: 'none',
                    border: 'none',
                    fontSize: '16px',
                    cursor: 'pointer'
                  }}
                >
                  ×
                </button>
              </div>
            )}

            {/* Team Search */}
            <div style={{
              paddingLeft: '16px',
              paddingRight: '16px',
              paddingTop: '12px',
              paddingBottom: '12px'
            }}>
              <label style={{
                display: 'flex',
                flexDirection: 'column',
                minWidth: '160px',
                height: '48px',
                width: '100%'
              }}>
                <div style={{
                  display: 'flex',
                  width: '100%',
                  flex: '1 1 0%',
                  alignItems: 'stretch',
                  borderRadius: '8px',
                  height: '100%'
                }}>
                  <div style={{
                    color: '#737373',
                    display: 'flex',
                    border: 'none',
                    backgroundColor: '#ededed',
                    alignItems: 'center',
                    justifyContent: 'center',
                    paddingLeft: '16px',
                    borderTopLeftRadius: '8px',
                    borderBottomLeftRadius: '8px',
                    borderRight: '0'
                  }}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor" viewBox="0 0 256 256">
                      <path d="M229.66,218.34l-50.07-50.06a88.11,88.11,0,1,0-11.31,11.31l50.06,50.07a8,8,0,0,0,11.32-11.32ZM40,112a72,72,0,1,1,72,72A72.08,72.08,0,0,1,40,112Z" />
                    </svg>
                  </div>
                  <input
                    placeholder="Search for a team"
                    style={{
                      display: 'flex',
                      width: '100%',
                      minWidth: '0',
                      flex: '1 1 0%',
                      resize: 'none',
                      overflow: 'hidden',
                      borderRadius: '8px',
                      color: '#141414',
                      outline: '0',
                      border: 'none',
                      backgroundColor: '#ededed',
                      height: '100%',
                      paddingLeft: '8px',
                      paddingRight: '16px',
                      borderTopLeftRadius: '0',
                      borderBottomLeftRadius: '0',
                      borderLeft: '0',
                      fontSize: '16px',
                      fontWeight: 'normal',
                      lineHeight: 'normal'
                    }}
                    value={teamSearchTerm}
                    onChange={(e) => setTeamSearchTerm(e.target.value)}
                    onKeyPress={handleKeyPress}
                  />
                  <button
                    onClick={handleTeamSearch}
                    disabled={loading}
                    style={{
                      backgroundColor: '#141414',
                      color: 'white',
                      paddingLeft: '16px',
                      paddingRight: '16px',
                      borderTopRightRadius: '8px',
                      borderBottomRightRadius: '8px',
                      border: 'none',
                      cursor: loading ? 'not-allowed' : 'pointer',
                      opacity: loading ? 0.5 : 1,
                      transition: 'colors 0.3s'
                    }}
                  >
                    {loading ? 'Searching...' : 'Search'}
                  </button>
                </div>
              </label>
            </div>

            {/* Team Selection Dropdown */}
            <div style={{
              paddingLeft: '16px',
              paddingRight: '16px',
              paddingTop: '12px',
              paddingBottom: '12px'
            }}>
              <select
                value={selectedTeamId || ''}
                onChange={(e) => {
                  const teamId = parseInt(e.target.value);
                  setSelectedTeamId(teamId);
                  setError(null);
                }}
                style={{
                  width: '100%',
                  padding: '12px',
                  border: '1px solid #dbdbdb',
                  borderRadius: '8px',
                  backgroundColor: 'white',
                  color: '#141414',
                  outline: '0',
                  fontSize: '16px'
                }}
              >
                <option value="">Select a team</option>
                {availableTeams.map((team) => (
                  <option key={team.teamId} value={team.teamId}>
                    {team.teamName}
                  </option>
                ))}
              </select>
            </div>

            {/* Loading indicator for team data */}
            {loading && (
              <div style={{
                paddingLeft: '16px',
                paddingRight: '16px',
                paddingTop: '12px',
                paddingBottom: '12px',
                textAlign: 'center'
              }}>
                <div style={{ color: '#737373' }}>
                  {teamData ? 'Updating team data...' : 'Loading team data...'}
                </div>
              </div>
            )}

            {/* Team Overview */}
            {teamData && (
              <>
                <h2 style={{
                  color: '#141414',
                  fontSize: '22px',
                  fontWeight: 'bold',
                  lineHeight: 'tight',
                  letterSpacing: '-0.015em',
                  paddingLeft: '16px',
                  paddingRight: '16px',
                  paddingBottom: '12px',
                  paddingTop: '20px',
                  margin: 0
                }}>Team Overview</h2>
                <div style={{ padding: '16px' }}>
                  <div style={{
                    display: 'flex',
                    alignItems: 'stretch',
                    justifyContent: 'space-between',
                    gap: '16px',
                    borderRadius: '8px',
                    border: '1px solid #dbdbdb',
                    padding: '24px',
                    backgroundColor: 'white'
                  }}>
                    <div style={{
                      display: 'flex',
                      flexDirection: 'column',
                      gap: '4px',
                      flex: '2 2 0px'
                    }}>
                      <p style={{
                        color: '#141414',
                        fontSize: '16px',
                        fontWeight: 'bold',
                        lineHeight: 'tight',
                        margin: 0
                      }}>{teamData.teamName}</p>
                      <p style={{
                        color: '#737373',
                        fontSize: '14px',
                        fontWeight: 'normal',
                        lineHeight: 'normal',
                        margin: 0
                      }}>
                        Stadium: {teamData.homeStadium || 'Unknown'} | League: {teamData.league || 'PSL'}
                      </p>
                      {teamData.teamAlias && (
                        <p style={{
                          color: '#737373',
                          fontSize: '14px',
                          fontWeight: 'normal',
                          lineHeight: 'normal',
                          margin: 0
                        }}>
                          Also known as: {teamData.teamAlias}
                        </p>
                      )}
                    </div>
                    <div style={{
                      width: '128px',
                      height: '128px',
                      backgroundColor: '#e5e7eb',
                      borderRadius: '8px',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      border: '1px solid #dbdbdb'
                    }}>
                      <span style={{
                        color: '#6b7280',
                        fontSize: '12px'
                      }}>Team Logo</span>
                    </div>
                  </div>
                </div>
              </>
            )}

            {/* Season Statistics */}
            <h2 style={{
              color: '#141414',
              fontSize: '22px',
              fontWeight: 'bold',
              lineHeight: 'tight',
              letterSpacing: '-0.015em',
              paddingLeft: '16px',
              paddingRight: '16px',
              paddingBottom: '12px',
              paddingTop: '20px',
              margin: 0
            }}>Season Statistics</h2>
            <div style={{
              paddingLeft: '16px',
              paddingRight: '16px',
              paddingTop: '12px',
              paddingBottom: '12px'
            }}>
              {seasonStats.length > 0 ? (
                <div style={{
                  display: 'flex',
                  overflow: 'hidden',
                  borderRadius: '8px',
                  border: '1px solid #dbdbdb',
                  backgroundColor: 'white'
                }}>
                  <table style={{ flex: '1 1 0%', borderCollapse: 'collapse' }}>
                    <thead>
                      <tr style={{ backgroundColor: '#f9fafb' }}>
                        <th style={{
                          paddingLeft: '16px',
                          paddingRight: '16px',
                          paddingTop: '12px',
                          paddingBottom: '12px',
                          textAlign: 'left',
                          color: '#141414',
                          width: '400px',
                          fontSize: '14px',
                          fontWeight: '500',
                          lineHeight: 'normal',
                          border: 'none'
                        }}>Statistic</th>
                        <th style={{
                          paddingLeft: '16px',
                          paddingRight: '16px',
                          paddingTop: '12px',
                          paddingBottom: '12px',
                          textAlign: 'left',
                          color: '#141414',
                          width: '400px',
                          fontSize: '14px',
                          fontWeight: '500',
                          lineHeight: 'normal',
                          border: 'none'
                        }}>Value</th>
                      </tr>
                    </thead>
                    <tbody>
                      {seasonStats.map((stat, index) => (
                        <tr key={index} style={{
                          borderTop: '1px solid #dbdbdb'
                        }}>
                          <td style={{
                            height: '72px',
                            paddingLeft: '16px',
                            paddingRight: '16px',
                            paddingTop: '8px',
                            paddingBottom: '8px',
                            width: '400px',
                            color: '#141414',
                            fontSize: '14px',
                            fontWeight: 'normal',
                            lineHeight: 'normal'
                          }}>
                            {stat.statistic}
                          </td>
                          <td style={{
                            height: '72px',
                            paddingLeft: '16px',
                            paddingRight: '16px',
                            paddingTop: '8px',
                            paddingBottom: '8px',
                            width: '400px',
                            color: '#737373',
                            fontSize: '14px',
                            fontWeight: 'normal',
                            lineHeight: 'normal'
                          }}>
                            {stat.value}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div style={{
                  textAlign: 'center',
                  paddingTop: '32px',
                  paddingBottom: '32px',
                  color: '#737373',
                  backgroundColor: 'white',
                  borderRadius: '8px',
                  border: '1px solid #dbdbdb'
                }}>
                  {selectedTeamId ? 'Loading statistics...' : 'Please select a team to view statistics.'}
                </div>
              )}
            </div>

            {/* Home vs. Away Performance */}
            <h2 style={{
              color: '#141414',
              fontSize: '22px',
              fontWeight: 'bold',
              lineHeight: 'tight',
              letterSpacing: '-0.015em',
              paddingLeft: '16px',
              paddingRight: '16px',
              paddingBottom: '12px',
              paddingTop: '20px',
              margin: 0
            }}>Home vs. Away Performance</h2>
            <div style={{
              display: 'flex',
              flexWrap: 'wrap',
              gap: '16px',
              padding: '16px'
            }}>
              <div style={{
                display: 'flex',
                minWidth: '158px',
                flex: '1 1 0%',
                flexDirection: 'column',
                gap: '8px',
                borderRadius: '8px',
                padding: '24px',
                border: '1px solid #dbdbdb',
                backgroundColor: 'white'
              }}>
                <p style={{
                  color: '#141414',
                  fontSize: '16px',
                  fontWeight: '500',
                  lineHeight: 'normal',
                  margin: 0
                }}>Away Wins</p>
                <p style={{
                  color: '#141414',
                  letterSpacing: '-0.025em',
                  fontSize: '24px',
                  fontWeight: 'bold',
                  lineHeight: 'tight',
                  margin: 0
                }}>{homeAwayStats.awayWins}</p>
              </div>
            </div>

            {/* Match Statistics */}
            {matchStats && (
              <>
                <h2 style={{
                  color: '#141414',
                  fontSize: '22px',
                  fontWeight: 'bold',
                  lineHeight: 'tight',
                  letterSpacing: '-0.015em',
                  paddingLeft: '16px',
                  paddingRight: '16px',
                  paddingBottom: '12px',
                  paddingTop: '20px',
                  margin: 0
                }}>Match Statistics</h2>
                <div style={{
                  display: 'flex',
                  flexWrap: 'wrap',
                  gap: '16px',
                  padding: '16px'
                }}>
                  <div style={{
                    display: 'flex',
                    minWidth: '158px',
                    flex: '1 1 0%',
                    flexDirection: 'column',
                    gap: '8px',
                    borderRadius: '8px',
                    padding: '24px',
                    border: '1px solid #dbdbdb',
                    backgroundColor: 'white'
                  }}>
                    <p style={{
                      color: '#141414',
                      fontSize: '16px',
                      fontWeight: '500',
                      lineHeight: 'normal',
                      margin: 0
                    }}>Total Shots</p>
                    <p style={{
                      color: '#141414',
                      letterSpacing: '-0.025em',
                      fontSize: '24px',
                      fontWeight: 'bold',
                      lineHeight: 'tight',
                      margin: 0
                    }}>{matchStats.totalShots || 0}</p>
                  </div>
                  <div style={{
                    display: 'flex',
                    minWidth: '158px',
                    flex: '1 1 0%',
                    flexDirection: 'column',
                    gap: '8px',
                    borderRadius: '8px',
                    padding: '24px',
                    border: '1px solid #dbdbdb',
                    backgroundColor: 'white'
                  }}>
                    <p style={{
                      color: '#141414',
                      fontSize: '16px',
                      fontWeight: '500',
                      lineHeight: 'normal',
                      margin: 0
                    }}>Shots on Target</p>
                    <p style={{
                      color: '#141414',
                      letterSpacing: '-0.025em',
                      fontSize: '24px',
                      fontWeight: 'bold',
                      lineHeight: 'tight',
                      margin: 0
                    }}>{matchStats.shotsOnTarget || 0}</p>
                  </div>
                  <div style={{
                    display: 'flex',
                    minWidth: '158px',
                    flex: '1 1 0%',
                    flexDirection: 'column',
                    gap: '8px',
                    borderRadius: '8px',
                    padding: '24px',
                    border: '1px solid #dbdbdb',
                    backgroundColor: 'white'
                  }}>
                    <p style={{
                      color: '#141414',
                      fontSize: '16px',
                      fontWeight: '500',
                      lineHeight: 'normal',
                      margin: 0
                    }}>Avg Possession</p>
                    <p style={{
                      color: '#141414',
                      letterSpacing: '-0.025em',
                      fontSize: '24px',
                      fontWeight: 'bold',
                      lineHeight: 'tight',
                      margin: 0
                    }}>{matchStats.avgPossession ? `${matchStats.avgPossession}%` : '0%'}</p>
                  </div>
                  <div style={{
                    display: 'flex',
                    minWidth: '158px',
                    flex: '1 1 0%',
                    flexDirection: 'column',
                    gap: '8px',
                    borderRadius: '8px',
                    padding: '24px',
                    border: '1px solid #dbdbdb',
                    backgroundColor: 'white'
                  }}>
                    <p style={{
                      color: '#141414',
                      fontSize: '16px',
                      fontWeight: '500',
                      lineHeight: 'normal',
                      margin: 0
                    }}>Yellow Cards</p>
                    <p style={{
                      color: '#141414',
                      letterSpacing: '-0.025em',
                      fontSize: '24px',
                      fontWeight: 'bold',
                      lineHeight: 'tight',
                      margin: 0
                    }}>{matchStats.yellowCards || 0}</p>
                  </div>
                  <div style={{
                    display: 'flex',
                    minWidth: '158px',
                    flex: '1 1 0%',
                    flexDirection: 'column',
                    gap: '8px',
                    borderRadius: '8px',
                    padding: '24px',
                    border: '1px solid #dbdbdb',
                    backgroundColor: 'white'
                  }}>
                    <p style={{
                      color: '#141414',
                      fontSize: '16px',
                      fontWeight: '500',
                      lineHeight: 'normal',
                      margin: 0
                    }}>Red Cards</p>
                    <p style={{
                      color: '#141414',
                      letterSpacing: '-0.025em',
                      fontSize: '24px',
                      fontWeight: 'bold',
                      lineHeight: 'tight',
                      margin: 0
                    }}>{matchStats.redCards || 0}</p>
                  </div>
                  <div style={{
                    display: 'flex',
                    minWidth: '158px',
                    flex: '1 1 0%',
                    flexDirection: 'column',
                    gap: '8px',
                    borderRadius: '8px',
                    padding: '24px',
                    border: '1px solid #dbdbdb',
                    backgroundColor: 'white'
                  }}>
                    <p style={{
                      color: '#141414',
                      fontSize: '16px',
                      fontWeight: '500',
                      lineHeight: 'normal',
                      margin: 0
                    }}>Corners</p>
                    <p style={{
                      color: '#141414',
                      letterSpacing: '-0.025em',
                      fontSize: '24px',
                      fontWeight: 'bold',
                      lineHeight: 'tight',
                      margin: 0
                    }}>{matchStats.corners || 0}</p>
                  </div>
                </div>
              </>
            )}

            {/* Upcoming Fixtures */}
            <h2 style={{
              color: '#141414',
              fontSize: '22px',
              fontWeight: 'bold',
              lineHeight: 'tight',
              letterSpacing: '-0.015em',
              paddingLeft: '16px',
              paddingRight: '16px',
              paddingBottom: '12px',
              paddingTop: '20px',
              margin: 0
            }}>Upcoming Fixtures</h2>
            <div style={{
              paddingLeft: '16px',
              paddingRight: '16px',
              paddingTop: '12px',
              paddingBottom: '12px'
            }}>
              {fixtures.length > 0 ? (
                <div style={{
                  display: 'flex',
                  overflow: 'hidden',
                  borderRadius: '8px',
                  border: '1px solid #dbdbdb',
                  backgroundColor: 'white'
                }}>
                  <table style={{ flex: '1 1 0%', borderCollapse: 'collapse' }}>
                    <thead>
                      <tr style={{ backgroundColor: '#f9fafb' }}>
                        <th style={{
                          paddingLeft: '16px',
                          paddingRight: '16px',
                          paddingTop: '12px',
                          paddingBottom: '12px',
                          textAlign: 'left',
                          color: '#141414',
                          width: '25%',
                          fontSize: '14px',
                          fontWeight: '500',
                          lineHeight: 'normal',
                          border: 'none'
                        }}>Opponent</th>
                        <th style={{
                          paddingLeft: '16px',
                          paddingRight: '16px',
                          paddingTop: '12px',
                          paddingBottom: '12px',
                          textAlign: 'left',
                          color: '#141414',
                          width: '25%',
                          fontSize: '14px',
                          fontWeight: '500',
                          lineHeight: 'normal',
                          border: 'none'
                        }}>Date/Time</th>
                        <th style={{
                          paddingLeft: '16px',
                          paddingRight: '16px',
                          paddingTop: '12px',
                          paddingBottom: '12px',
                          textAlign: 'left',
                          color: '#141414',
                          width: '25%',
                          fontSize: '14px',
                          fontWeight: '500',
                          lineHeight: 'normal',
                          border: 'none'
                        }}>Venue</th>
                        <th style={{
                          paddingLeft: '16px',
                          paddingRight: '16px',
                          paddingTop: '12px',
                          paddingBottom: '12px',
                          textAlign: 'left',
                          color: '#141414',
                          width: '25%',
                          fontSize: '14px',
                          fontWeight: '500',
                          lineHeight: 'normal',
                          border: 'none'
                        }}>Home/Away</th>
                      </tr>
                    </thead>
                    <tbody>
                      {fixtures.map((fixture, index) => (
                        <tr key={index} style={{
                          borderTop: '1px solid #dbdbdb'
                        }}>
                          <td style={{
                            paddingLeft: '16px',
                            paddingRight: '16px',
                            paddingTop: '12px',
                            paddingBottom: '12px',
                            color: '#141414',
                            fontSize: '14px',
                            fontWeight: 'normal',
                            lineHeight: 'normal'
                          }}>
                            {fixture.opponent}
                          </td>
                          <td style={{
                            paddingLeft: '16px',
                            paddingRight: '16px',
                            paddingTop: '12px',
                            paddingBottom: '12px',
                            color: '#737373',
                            fontSize: '14px',
                            fontWeight: 'normal',
                            lineHeight: 'normal'
                          }}>
                            {fixture.dateTime}
                          </td>
                          <td style={{
                            paddingLeft: '16px',
                            paddingRight: '16px',
                            paddingTop: '12px',
                            paddingBottom: '12px',
                            color: '#737373',
                            fontSize: '14px',
                            fontWeight: 'normal',
                            lineHeight: 'normal'
                          }}>
                            {fixture.venue}
                          </td>
                          <td style={{
                            paddingLeft: '16px',
                            paddingRight: '16px',
                            paddingTop: '12px',
                            paddingBottom: '12px',
                            color: '#737373',
                            fontSize: '14px',
                            fontWeight: 'normal',
                            lineHeight: 'normal'
                          }}>
                            <span style={{
                              paddingLeft: '8px',
                              paddingRight: '8px',
                              paddingTop: '4px',
                              paddingBottom: '4px',
                              borderRadius: '4px',
                              fontSize: '12px',
                              fontWeight: '500',
                              backgroundColor: fixture.homeAway === 'Home' ? '#dcfce7' : '#dbeafe',
                              color: fixture.homeAway === 'Home' ? '#166534' : '#1e40af'
                            }}>
                              {fixture.homeAway}
                            </span>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div style={{
                  textAlign: 'center',
                  paddingTop: '32px',
                  paddingBottom: '32px',
                  color: '#737373',
                  backgroundColor: 'white',
                  borderRadius: '8px',
                  border: '1px solid #dbdbdb'
                }}>
                  {selectedTeamId ? 'No upcoming fixtures available for this team.' : 'Please select a team to view fixtures.'}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TeamStatisticsPage;
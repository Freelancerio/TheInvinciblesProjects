# TheInvinciblesProjects ⚽📊💸

 SmartBet is a sports statistics & betting platform – combining deep football analytics with a simple betting workflow.  
 Built with **Spring Boot, React, PostgreSQL, and Supabase**, and deployed with **CI/CD** from day one.

---

## 🚀 Project Overview
TheInvinciblesProjects is a web platform that blends **advanced football statistics** with a **straightforward betting flow**.  

- Initial focus: **South African Premiership**, with English Premier League planned next.  
- Explore up to **3 years of historic data** and place informed bets using standard stats and our **custom analytics models**.  

### 🎯 Core Goals
- Deliver a clean, fast UI for browsing teams, matches, and head-to-head stats.  
- Provide advanced analytics that go beyond basic box scores.  
- Enable a smooth betting flow with deposits, withdrawals, and bet tracking.  
- Ensure system security and CI/CD deployability from day one.  

---

## 🏗️ System Architecture
- **Client:** React app (deployed on Vercel).  
- **API:** Spring Boot REST service (hosted on Railway).  
- **Database:** PostgreSQL on Supabase (managed Postgres + auth rules at API layer).  
- **Auth:** Firebase Authentication (Google login , Github Login or Facebook Login).  

---

## 🔧 Feature Set
### 📊 Statistics Module
- **Standard Stats:** goals for/against, shots, possession, cards, pass accuracy, home/away results, 5-game form.  
- **Advanced Stats (custom):**
  - Momentum Index – weighted recent form over last *N* matches.  
  - Clutch Performance Score – output in last 15 minutes vs match average.  
  - Consistency Metric – variance of results over recent fixtures.  
  - Odds vs Performance – market odds vs actual outcomes.  

### 💸 Betting Module
- User Prediction: select fixture + outcome, submit bet.  
- System Prediction: model-driven suggestions based on analytics.  
- Wallet: deposits, withdrawals, transaction history.  
- Bet Tracking: open/settled bets, returns, and yield.  

### 👥 Accounts & Roles
- Firebase Authentication (Google).  
- Roles:  
  - **User** – place bets, view stats.  
  - **Admin** – manage fixtures cache, recompute metrics, manage users.  

---

## 📂 Folder Structure
```
├── Backend/ # Spring Boot REST API
├── Frontend/ # React frontend (Vercel)
├── documentation/ # Project documentation (Astro site)
├── scripts/ # Automation scripts
├── target/classes/ # Build artifacts
├── HELP.md # Additional usage docs
├── README.md # This file
├── pom.xml # Maven config for Backend
```


---

## ⚡ Getting Started

### 1️. Clone the repository
```bash
git clone https://github.com/stephan0709/TheInvinciblesProjects.git
cd TheInvinciblesProjects
```

### 2️. Backend Setup (Spring Boot API)
```
./mvnw clean install   # builds the project
./mvnw spring-boot:run # runs the API server
```

### 3️. Frontend Setup (React App)
```
cd Frontend
npm install
npm run dev
```

### 🤝 Contribution

**We follow GitLab Flow branching model + Scrum process.**
- main → production-ready
- develop → integration branch
- feature/ → new features
- hotfix/ → urgent fixes
- Pull requests are welcome


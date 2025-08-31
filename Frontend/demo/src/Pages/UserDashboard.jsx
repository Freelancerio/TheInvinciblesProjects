import React, { useEffect, useState } from "react";
import StatCard from "../Components/StatsCard";
import Section from "../Components/Section";
import "./UserDashboard.css";

function UserDashboard() {
    const [user, setUser] = useState(null);
    const [stats, setStats] = useState(null);
    const [matches, setMatches] = useState([]);
    const [standings, setStandings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);

                // Fetch user info
                const userResponse = await fetch("http://localhost:8080/user-info", {
                    credentials: 'include'
                });

                if (!userResponse.ok) {
                    throw new Error(`Failed to fetch user info: ${userResponse.status}`);
                }

                const userData = await userResponse.json();
                console.log("User data:", userData);
                setUser(userData);

                // Fetch user stats
                const statsResponse = await fetch("http://localhost:8080/api/user/stats", {
                    credentials: 'include'
                });

                if (!statsResponse.ok) {
                    throw new Error(`Failed to fetch stats: ${statsResponse.status}`);
                }

                const statsData = await statsResponse.json();
                console.log("Stats data:", statsData);
                setStats(statsData);

                // Fetch matches data
                try {
                    const matchesResponse = await fetch("http://localhost:8080/api/database/psl/matches");
                    if (matchesResponse.ok) {
                        const matchesData = await matchesResponse.json();
                        console.log("Raw matches response:", matchesData);

                        // Extract matches array from the response
                        if (matchesData && matchesData.matches && Array.isArray(matchesData.matches)) {
                            setMatches(matchesData.matches);
                        } else {
                            console.warn("Unexpected matches data structure:", matchesData);
                            setMatches([]);
                        }
                    }
                } catch (matchError) {
                    console.error("Error fetching matches:", matchError);
                    setMatches([]);
                }

                // Fetch standings data
                try {
                    const standingsResponse = await fetch("http://localhost:8080/api/database/psl/standings");
                    if (standingsResponse.ok) {
                        const standingsData = await standingsResponse.json();
                        console.log("Raw standings response:", standingsData);

                        // Extract standings array from the response
                        if (standingsData && standingsData.standings && Array.isArray(standingsData.standings)) {
                            setStandings(standingsData.standings);
                        } else if (standingsData && standingsData.table && Array.isArray(standingsData.table)) {
                            setStandings(standingsData.table);
                        } else if (Array.isArray(standingsData)) {
                            setStandings(standingsData);
                        } else {
                            console.warn("Unexpected standings data structure:", standingsData);
                            setStandings([]);
                        }
                    }
                } catch (standingsError) {
                    console.error("Error fetching standings:", standingsError);
                    setStandings([]);
                }

            } catch (err) {
                console.error("Error fetching data:", err);
                setError(err.message);

                if (err.message.includes('401') || err.message.includes('unauthorized')) {
                    window.location.href = '/login?error=session_expired';
                }
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return (
            <div style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                height: '100vh',
                backgroundColor: '#f9f8fc',
                fontFamily: 'Inter, Noto Sans, sans-serif'
            }}>
                <div style={{ textAlign: 'center' }}>
                    <div style={{
                        width: '48px',
                        height: '48px',
                        border: '4px solid #ebe7f3',
                        borderTop: '4px solid #654e97',
                        borderRadius: '50%',
                        animation: 'spin 1s linear infinite',
                        margin: '0 auto 16px'
                    }}></div>
                    <div style={{ color: '#120e1b', fontWeight: '500' }}>Loading...</div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                height: '100vh',
                flexDirection: 'column',
                backgroundColor: '#f9f8fc',
                fontFamily: 'Inter, Noto Sans, sans-serif'
            }}>
                <div style={{
                    color: '#e53e3e',
                    marginBottom: '16px',
                    textAlign: 'center',
                    maxWidth: '400px',
                    padding: '24px',
                    backgroundColor: 'white',
                    borderRadius: '8px',
                    border: '1px solid #fed7d7'
                }}>
                    <h3 style={{ fontWeight: 'bold', fontSize: '18px', marginBottom: '8px' }}>Something went wrong</h3>
                    <p style={{ fontSize: '14px' }}>Error: {error}</p>
                </div>
                <button
                    onClick={() => window.location.reload()}
                    style={{
                        padding: '12px 24px',
                        backgroundColor: '#654e97',
                        color: 'white',
                        borderRadius: '8px',
                        border: 'none',
                        fontWeight: '500',
                        cursor: 'pointer'
                    }}
                    onMouseOver={(e) => e.target.style.backgroundColor = '#5a4285'}
                    onMouseOut={(e) => e.target.style.backgroundColor = '#654e97'}
                >
                    Try Again
                </button>
            </div>
        );
    }

    if (!user) {
        return (
            <div style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                height: '100vh',
                backgroundColor: '#f9f8fc',
                fontFamily: 'Inter, Noto Sans, sans-serif'
            }}>
                <div style={{ textAlign: 'center', color: '#120e1b' }}>
                    <h3 style={{ fontWeight: 'bold', fontSize: '18px', marginBottom: '8px' }}>No Data Available</h3>
                    <p style={{ fontSize: '14px', color: '#654e97' }}>Unable to load user information</p>
                </div>
            </div>
        );
    }

    return (
        <div style={{
            fontFamily: 'Inter, Noto Sans, sans-serif',
            backgroundColor: '#f9f8fc',
            minHeight: '100vh',
            width: '100%'
        }}>
            {/* HEADER */}
            <header style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                borderBottom: '1px solid #ebe7f3',
                padding: '12px 40px',
                backgroundColor: 'white',
                boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)'
            }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '16px', color: '#120e1b' }}>
                    <div style={{ width: '32px', height: '32px' }}>
                        <svg viewBox="0 0 48 48" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                            <path d="M44 11.27C44 14 39.8 16.4 33.7 17.6 39.8 18.9 44 21.3 44 24s-4.2 5.1-10.3 6.4C39.8 31.6 44 34 44 36.7 44 40.7 35 44 24 44S4 40.7 4 36.7c0-2.7 4.2-5.1 10.3-6.4C8.2 29.1 4 26.7 4 24s4.2-5.1 10.3-6.4C8.2 16.4 4 14 4 11.3 4 7.3 13 4 24 4s20 3.3 20 7.3z" />
                        </svg>
                    </div>
                    <h2 style={{ fontSize: '20px', fontWeight: 'bold', color: '#654e97' }}>SmartBet</h2>
                </div>
                <div style={{ display: 'flex', flex: 1, justifyContent: 'flex-end', gap: '32px' }}>
                    <nav style={{ display: 'flex', alignItems: 'center', gap: '36px' }}>
                        {["Home", "Live", "Fixtures", "Results", "Promotions"].map((link, i) => (
                            <a
                                key={i}
                                href="#"
                                style={{
                                    fontSize: '14px',
                                    fontWeight: '500',
                                    color: '#120e1b',
                                    textDecoration: 'none'
                                }}
                                onMouseOver={(e) => e.target.style.color = '#654e97'}
                                onMouseOut={(e) => e.target.style.color = '#120e1b'}
                            >
                                {link}
                            </a>
                        ))}
                    </nav>
                    <button style={{
                        height: '40px',
                        padding: '0 16px',
                        borderRadius: '8px',
                        backgroundColor: '#ebe7f3',
                        fontSize: '14px',
                        fontWeight: 'bold',
                        border: 'none',
                        cursor: 'pointer'
                    }}>
                        Help
                    </button>
                    <div style={{
                        width: '40px',
                        height: '40px',
                        borderRadius: '50%',
                        backgroundColor: '#654e97',
                        backgroundImage: user.avatarUrl ? `url(${user.avatarUrl})` : 'none',
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: 'white',
                        fontWeight: 'bold',
                        fontSize: '14px',
                        border: '2px solid #ebe7f3'
                    }}>
                        {!user.avatarUrl && (user.name ? user.name.charAt(0).toUpperCase() : 'U')}
                    </div>
                </div>
            </header>

            {/* CONTENT */}
            <div style={{ padding: '32px 160px', display: 'flex', justifyContent: 'center' }}>
                <div style={{ display: 'flex', flexDirection: 'column', maxWidth: '960px', width: '100%' }}>
                    <h1 style={{
                        fontSize: '32px',
                        fontWeight: 'bold',
                        padding: '0 16px',
                        marginBottom: '24px',
                        color: '#120e1b'
                    }}>
                        Welcome back, {user.name || 'User'}
                    </h1>

                    {/* STATS */}
                    <div style={{
                        display: 'flex',
                        flexWrap: 'wrap',
                        gap: '16px',
                        padding: '16px',
                        marginBottom: '24px'
                    }}>
                        <StatCard
                            title="Total Bets Placed"
                            value={stats?.totalBets || 0}
                        />
                        <StatCard
                            title="Win Rate"
                            value={`${stats?.winRate || 0}%`}
                        />
                        <StatCard
                            title="Available Balance"
                            value={`R${(stats?.winnings || 0).toLocaleString('en-ZA', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`}
                        />
                        <StatCard
                            title="Available Balance"
                            value={`R${(stats?.balance || 0).toLocaleString('en-ZA', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`}
                        />
                    </div>

                    {/* MATCHES */}
                    <Section title="Favorite Teams - Upcoming Matches">
                        {Array.isArray(matches) && matches.length > 0 ? (
                            <div style={{ overflowX: 'auto' }}>
                                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                                    <thead>
                                        <tr style={{ backgroundColor: '#f5f3f8', borderBottom: '1px solid #ebe7f3' }}>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'left',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b'
                                            }}>
                                                Match
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'left',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b'
                                            }}>
                                                Date/Time & Status
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'left',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b'
                                            }}>
                                                Action
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {matches
                                            .filter(match => match.status === 'scheduled') // Only show upcoming matches
                                            .slice(0, 5)
                                            .map((match, i) => {
                                                // Format the date
                                                const formatMatchDate = (dateTime) => {
                                                    try {
                                                        const date = new Date(dateTime);
                                                        return date.toLocaleDateString('en-US', {
                                                            weekday: 'short',
                                                            month: 'short',
                                                            day: 'numeric',
                                                            hour: '2-digit',
                                                            minute: '2-digit'
                                                        });
                                                    } catch (e) {
                                                        return dateTime;
                                                    }
                                                };

                                                const matchDisplay = `${match.home_team || match.home_team_alias || 'Home'} vs ${match.away_team || match.away_team_alias || 'Away'}`;
                                                const dateDisplay = formatMatchDate(match.match_datetime);
                                                const isCompleted = match.status === 'completed' || match.match_status === 'finished';
                                                const hasScore = match.home_team_score !== null && match.away_team_score !== null;

                                                return (
                                                    <tr
                                                        key={match.fixture_id || i}
                                                        style={{
                                                            borderTop: '1px solid #ebe7f3'
                                                        }}
                                                        onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#faf9fc'}
                                                        onMouseOut={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                                                    >
                                                        <td style={{ padding: '12px 16px', color: '#120e1b' }}>
                                                            <div style={{ fontWeight: '500' }}>
                                                                {matchDisplay}
                                                            </div>
                                                            {match.venue && (
                                                                <div style={{ fontSize: '12px', color: '#654e97', marginTop: '4px' }}>
                                                                    📍 {match.venue}
                                                                </div>
                                                            )}
                                                            {hasScore && (
                                                                <div style={{ fontSize: '14px', color: '#059669', marginTop: '4px', fontWeight: 'bold' }}>
                                                                    Score: {match.home_team_score} - {match.away_team_score}
                                                                </div>
                                                            )}
                                                        </td>
                                                        <td style={{ padding: '12px 16px', color: '#654e97', fontSize: '14px' }}>
                                                            <div>{dateDisplay}</div>
                                                            {match.status && (
                                                                <div style={{
                                                                    fontSize: '12px',
                                                                    marginTop: '4px',
                                                                    color: isCompleted ? '#059669' : match.status === 'scheduled' ? '#3b82f6' : '#f59e0b',
                                                                    fontWeight: '500',
                                                                    textTransform: 'capitalize'
                                                                }}>
                                                                    {match.status}
                                                                </div>
                                                            )}
                                                        </td>
                                                        <td style={{ padding: '12px 16px' }}>
                                                            {!isCompleted ? (
                                                                <button style={{
                                                                    color: '#654e97',
                                                                    fontWeight: 'bold',
                                                                    fontSize: '14px',
                                                                    background: 'none',
                                                                    border: 'none',
                                                                    cursor: 'pointer',
                                                                    padding: '4px 8px',
                                                                    borderRadius: '4px',
                                                                    transition: 'all 0.2s'
                                                                }}
                                                                    onMouseOver={(e) => {
                                                                        e.target.style.backgroundColor = '#654e97';
                                                                        e.target.style.color = 'white';
                                                                    }}
                                                                    onMouseOut={(e) => {
                                                                        e.target.style.backgroundColor = 'transparent';
                                                                        e.target.style.color = '#654e97';
                                                                    }}
                                                                >
                                                                    Bet Now
                                                                </button>
                                                            ) : (
                                                                <span style={{ color: '#6b7280', fontSize: '12px' }}>
                                                                    Completed
                                                                </span>
                                                            )}
                                                        </td>
                                                    </tr>
                                                );
                                            })}
                                    </tbody>
                                </table>
                            </div>
                        ) : (
                            <div style={{
                                textAlign: 'center',
                                padding: '32px',
                                color: '#654e97'
                            }}>
                                <p style={{ fontSize: '14px' }}>
                                    No upcoming matches available
                                </p>
                                <p style={{ fontSize: '12px', marginTop: '8px', color: '#9ca3af' }}>
                                    Debug: Matches array length: {matches?.length || 0}
                                </p>
                            </div>
                        )}
                    </Section>

                    {/* STANDINGS */}
                    <Section title="PSL League Standings - 2024 Season">
                        {Array.isArray(standings) && standings.length > 0 ? (
                            <div style={{ overflowX: 'auto' }}>
                                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                                    <thead>
                                        <tr style={{ backgroundColor: '#f5f3f8', borderBottom: '1px solid #ebe7f3' }}>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'left',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b',
                                                width: '60px'
                                            }}>
                                                Pos
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'left',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b'
                                            }}>
                                                Team
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'center',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b',
                                                width: '80px'
                                            }}>
                                                MP
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'center',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b',
                                                width: '80px'
                                            }}>
                                                W
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'center',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b',
                                                width: '80px'
                                            }}>
                                                D
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'center',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b',
                                                width: '80px'
                                            }}>
                                                L
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'center',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b',
                                                width: '100px'
                                            }}>
                                                GD
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'center',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b',
                                                width: '80px'
                                            }}>
                                                Pts
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {standings.map((team, i) => {
                                            // Determine position styling based on league position
                                            let positionStyle = {};
                                            if (i < 4) {
                                                // Top 4 - Champions League qualification
                                                positionStyle = {
                                                    backgroundColor: '#d1fae5',
                                                    borderLeft: '4px solid #059669'
                                                };
                                            } else if (i < 8) {
                                                // 5th-8th - Europa/Conference League
                                                positionStyle = {
                                                    backgroundColor: '#dbeafe',
                                                    borderLeft: '4px solid #3b82f6'
                                                };
                                            } else if (i >= standings.length - 3) {
                                                // Bottom 3 - Relegation zone
                                                positionStyle = {
                                                    backgroundColor: '#fef2f2',
                                                    borderLeft: '4px solid #ef4444'
                                                };
                                            }

                                            return (
                                                <tr
                                                    key={team.team_id || i}
                                                    style={{
                                                        borderTop: '1px solid #ebe7f3',
                                                        ...positionStyle
                                                    }}
                                                    onMouseOver={(e) => {
                                                        if (!positionStyle.backgroundColor) {
                                                            e.currentTarget.style.backgroundColor = '#faf9fc';
                                                        }
                                                    }}
                                                    onMouseOut={(e) => {
                                                        if (!positionStyle.backgroundColor) {
                                                            e.currentTarget.style.backgroundColor = 'transparent';
                                                        }
                                                    }}
                                                >
                                                    <td style={{
                                                        padding: '12px 16px',
                                                        color: '#654e97',
                                                        fontWeight: 'bold',
                                                        textAlign: 'center',
                                                        fontSize: '16px'
                                                    }}>
                                                        {team.position || i + 1}
                                                    </td>
                                                    <td style={{ padding: '12px 16px', color: '#120e1b' }}>
                                                        <div style={{ display: 'flex', flexDirection: 'column' }}>
                                                            <div style={{ fontWeight: '600', fontSize: '14px' }}>
                                                                {team.team_name || team.name || `Team ${i + 1}`}
                                                            </div>
                                                            <div style={{
                                                                fontSize: '12px',
                                                                color: '#654e97',
                                                                marginTop: '2px'
                                                            }}>
                                                                {team.team_alias || team.alias}
                                                                {team.home_stadium && (
                                                                    <span style={{ color: '#9ca3af' }}>
                                                                        {' • '}{team.home_stadium}
                                                                    </span>
                                                                )}
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td style={{
                                                        padding: '12px 16px',
                                                        color: '#654e97',
                                                        textAlign: 'center',
                                                        fontWeight: '500'
                                                    }}>
                                                        {team.matches_played || team.played || team.mp || 0}
                                                    </td>
                                                    <td style={{
                                                        padding: '12px 16px',
                                                        color: '#059669',
                                                        textAlign: 'center',
                                                        fontWeight: '500'
                                                    }}>
                                                        {team.wins || team.won || team.w || 0}
                                                    </td>
                                                    <td style={{
                                                        padding: '12px 16px',
                                                        color: '#f59e0b',
                                                        textAlign: 'center',
                                                        fontWeight: '500'
                                                    }}>
                                                        {team.draws || team.drawn || team.d || 0}
                                                    </td>
                                                    <td style={{
                                                        padding: '12px 16px',
                                                        color: '#ef4444',
                                                        textAlign: 'center',
                                                        fontWeight: '500'
                                                    }}>
                                                        {team.losses || team.lost || team.l || 0}
                                                    </td>
                                                    <td style={{
                                                        padding: '12px 16px',
                                                        color: '#654e97',
                                                        textAlign: 'center',
                                                        fontWeight: '600'
                                                    }}>
                                                        {team.goal_difference || team.goalDifference || team.gd ||
                                                            ((team.goals_for || 0) - (team.goals_against || 0)) || 0}
                                                    </td>
                                                    <td style={{
                                                        padding: '12px 16px',
                                                        color: '#120e1b',
                                                        textAlign: 'center',
                                                        fontWeight: 'bold',
                                                        fontSize: '16px'
                                                    }}>
                                                        {team.points || team.pts || 0}
                                                    </td>
                                                </tr>
                                            );
                                        })}
                                    </tbody>
                                </table>

                                {/* Legend */}
                                <div style={{
                                    marginTop: '16px',
                                    display: 'flex',
                                    flexWrap: 'wrap',
                                    gap: '16px',
                                    fontSize: '12px',
                                    color: '#6b7280'
                                }}>
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                                        <div style={{
                                            width: '12px',
                                            height: '12px',
                                            backgroundColor: '#059669',
                                            borderRadius: '2px'
                                        }}></div>
                                        <span>Champions League</span>
                                    </div>
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                                        <div style={{
                                            width: '12px',
                                            height: '12px',
                                            backgroundColor: '#3b82f6',
                                            borderRadius: '2px'
                                        }}></div>
                                        <span>Continental Competition</span>
                                    </div>
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                                        <div style={{
                                            width: '12px',
                                            height: '12px',
                                            backgroundColor: '#ef4444',
                                            borderRadius: '2px'
                                        }}></div>
                                        <span>Relegation Zone</span>
                                    </div>
                                </div>

                                {/* Additional info */}
                                <div style={{
                                    marginTop: '12px',
                                    fontSize: '11px',
                                    color: '#9ca3af',
                                    textAlign: 'center'
                                }}>
                                    MP: Matches Played • W: Won • D: Drawn • L: Lost • GD: Goal Difference • Pts: Points
                                </div>
                            </div>
                        ) : (
                            <div style={{
                                textAlign: 'center',
                                padding: '32px',
                                color: '#654e97'
                            }}>
                                <p style={{ fontSize: '14px' }}>
                                    No standings data available
                                </p>
                                <p style={{ fontSize: '12px', marginTop: '8px', color: '#9ca3af' }}>
                                    Debug: Standings array length: {standings?.length || 0}
                                </p>
                            </div>
                        )}
                    </Section>

                    {/* RECENT COMPLETED MATCHES */}
                    <Section title="Recent Match Results">
                        {Array.isArray(matches) && matches.length > 0 ? (
                            <div style={{ overflowX: 'auto' }}>
                                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                                    <thead>
                                        <tr style={{ backgroundColor: '#f5f3f8', borderBottom: '1px solid #ebe7f3' }}>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'left',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b'
                                            }}>
                                                Match
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'left',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b'
                                            }}>
                                                Score
                                            </th>
                                            <th style={{
                                                padding: '12px 16px',
                                                textAlign: 'left',
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                color: '#120e1b'
                                            }}>
                                                Date
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {matches
                                            .filter(match => match.status === 'completed' || match.home_team_score !== null)
                                            .slice(0, 5)
                                            .map((match, i) => {
                                                const formatMatchDate = (dateTime) => {
                                                    try {
                                                        const date = new Date(dateTime);
                                                        return date.toLocaleDateString('en-US', {
                                                            month: 'short',
                                                            day: 'numeric',
                                                            year: 'numeric'
                                                        });
                                                    } catch (e) {
                                                        return dateTime;
                                                    }
                                                };

                                                const matchDisplay = `${match.home_team || match.home_team_alias} vs ${match.away_team || match.away_team_alias}`;
                                                const scoreDisplay = (match.home_team_score !== null && match.away_team_score !== null)
                                                    ? `${match.home_team_score} - ${match.away_team_score}`
                                                    : 'N/A';

                                                return (
                                                    <tr
                                                        key={`completed-${match.fixture_id || i}`}
                                                        style={{ borderTop: '1px solid #ebe7f3' }}
                                                        onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#faf9fc'}
                                                        onMouseOut={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                                                    >
                                                        <td style={{ padding: '12px 16px', color: '#120e1b' }}>
                                                            <div style={{ fontWeight: '500' }}>
                                                                {matchDisplay}
                                                            </div>
                                                            {match.venue && (
                                                                <div style={{ fontSize: '12px', color: '#654e97', marginTop: '2px' }}>
                                                                    {match.venue}
                                                                </div>
                                                            )}
                                                        </td>
                                                        <td style={{ padding: '12px 16px', fontSize: '16px', fontWeight: 'bold', color: '#120e1b' }}>
                                                            {scoreDisplay}
                                                        </td>
                                                        <td style={{ padding: '12px 16px', color: '#654e97', fontSize: '14px' }}>
                                                            {formatMatchDate(match.match_datetime)}
                                                        </td>
                                                    </tr>
                                                );
                                            })}
                                    </tbody>
                                </table>
                                {matches.filter(match => match.status === 'completed' || match.home_team_score !== null).length === 0 && (
                                    <div style={{
                                        textAlign: 'center',
                                        padding: '32px',
                                        color: '#654e97'
                                    }}>
                                        <p style={{ fontSize: '14px' }}>No completed matches with scores available</p>
                                    </div>
                                )}
                            </div>
                        ) : (
                            <div style={{
                                textAlign: 'center',
                                padding: '32px',
                                color: '#654e97'
                            }}>
                                <p style={{ fontSize: '14px' }}>No match results available</p>
                            </div>
                        )}
                    </Section>
                </div>
            </div>

            <style jsx>{`
                @keyframes spin {
                    from { transform: rotate(0deg); }
                    to { transform: rotate(360deg); }
                }
            `}</style>
        </div>
    );
}

export default UserDashboard;
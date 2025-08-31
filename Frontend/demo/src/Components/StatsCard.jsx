import React from "react";

function StatCard({ title, value }) {
    return (
        <div style={{
            backgroundColor: 'white',
            borderRadius: '8px',
            border: '1px solid #ebe7f3',
            padding: '24px',
            boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
            minWidth: '200px',
            flex: '1',
            transition: 'all 0.3s ease'
        }}
        onMouseOver={(e) => {
            e.currentTarget.style.boxShadow = '0 4px 12px rgba(101, 78, 151, 0.15)';
            e.currentTarget.style.transform = 'translateY(-2px)';
        }}
        onMouseOut={(e) => {
            e.currentTarget.style.boxShadow = '0 1px 3px rgba(0, 0, 0, 0.1)';
            e.currentTarget.style.transform = 'translateY(0)';
        }}
        >
            <div style={{ display: 'flex', flexDirection: 'column' }}>
                <h3 style={{
                    fontSize: '14px',
                    fontWeight: '500',
                    color: '#654e97',
                    marginBottom: '8px'
                }}>
                    {title}
                </h3>
                <div style={{
                    fontSize: '24px',
                    fontWeight: 'bold',
                    color: '#120e1b'
                }}>
                    {value}
                </div>
            </div>
        </div>
    );
}

export default StatCard;
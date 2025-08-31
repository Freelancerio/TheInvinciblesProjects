import React from "react";

function Section({ title, children }) {
    return (
        <div style={{
            backgroundColor: 'white',
            borderRadius: '8px',
            border: '1px solid #ebe7f3',
            boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
            marginBottom: '24px',
            overflow: 'hidden'
        }}>
            <div style={{
                borderBottom: '1px solid #ebe7f3',
                padding: '16px 24px',
                backgroundColor: '#faf9fc'
            }}>
                <h2 style={{
                    fontSize: '18px',
                    fontWeight: '600',
                    color: '#120e1b',
                    margin: 0
                }}>
                    {title}
                </h2>
            </div>
            <div style={{ padding: '24px' }}>
                {children}
            </div>
        </div>
    );
}

export default Section;
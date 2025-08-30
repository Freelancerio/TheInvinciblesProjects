import React from "react";


function StatCard({ title, value }) {
    return (
        <div className="flex flex-1 min-w-[158px] flex-col gap-2 rounded-lg p-6 border border-[#d7d0e7]">
            <p className="text-base font-medium">{title}</p>
            <p className="text-2xl font-bold">{value}</p>
        </div>
    );
}

function Section({ title, children }) {
    return (
        <>
            <h2 className="text-[22px] font-bold px-4 pb-3 pt-5">{title}</h2>
            <div className="px-4 py-3 flex overflow-hidden rounded-lg border border-[#d7d0e7] bg-[#f9f8fc]">
                {children}
            </div>
        </>
    );
}

export StatCard;
export Section;
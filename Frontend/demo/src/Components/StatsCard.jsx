

function StatCard({ title, value }) {
    return (
        <div className="flex flex-1 min-w-[158px] flex-col gap-2 rounded-lg p-6 border border-[#d7d0e7]">
            <p className="text-base font-medium">{title}</p>
            <p className="text-2xl font-bold">{value}</p>
        </div>
    );
}

export default StatCard;


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


export default Section;
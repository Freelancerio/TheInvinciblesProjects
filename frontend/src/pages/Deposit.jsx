import React from "react";
import { AddFundsSection } from "./sections/AddFundsSection.jsx";
import { DepositHistorySection } from "./sections/DepositHistorySection.jsx";
import { PaymentMethodSection } from "./sections/PaymentMethodSection.jsx";

export const Deposit = () => {
    return (
        <main
            className="flex flex-col items-start relative bg-[#1e1e2f] translate-y-[-1rem] animate-fade-in opacity-0"
            data-model-id="1:1438"
        >
            <div className="flex flex-col min-h-screen items-start relative self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f]">
                <div className="flex items-start self-stretch w-full flex-col relative flex-[0_0_auto]">
                    <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms] w-full">
                        <AddFundsSection />
                    </div>
                    <div className="items-start justify-start gap-6 px-6 py-5 flex-1 grow flex relative self-stretch w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms]">
                        <div className="w-[68%]">
                            <PaymentMethodSection />
                        </div>
                        <div className="w-[28%]">
                            <DepositHistorySection />
                        </div>
                    </div>
                </div>
            </div>
        </main>
    );
};

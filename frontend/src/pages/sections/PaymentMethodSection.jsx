import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
    ToggleGroup,
    ToggleGroupItem,
} from "@/components/ui/toggle-group";

export const PaymentMethodSection = () => {
    const [selectedAmount, setSelectedAmount] = useState("");
    const [paymentMethod, setPaymentMethod] = useState("Card");

    const amountOptions = [
        { value: "20", label: "R20" },
        { value: "50", label: "R50" },
        { value: "100", label: "R100" },
        { value: "200", label: "R200" },
    ];

    const paymentMethods = [
        { value: "Card", label: "Card" },
        { value: "Bank Transfer", label: "Bank Transfer" },
        { value: "E-Wallet", label: "E-Wallet" },
    ];

    return (
        <div className="flex flex-col max-w-[920px] items-start relative flex-1 grow">
            <div className="flex flex-wrap justify-around gap-[12px_12px] p-4 self-stretch w-full items-start relative flex-[0_0_auto]">
                <div className="flex flex-col w-72 items-start relative">
                    <h1 className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:0ms] relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-bold text-[#e8edf5] text-[32px] tracking-[0] leading-10">
                        Add Funds
                    </h1>
                </div>
            </div>

            <div className="inline-flex flex-wrap max-w-[480px] items-end gap-[16px_16px] px-4 py-3 relative flex-[0_0_auto]">
                <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms] flex flex-col min-w-40 items-start relative flex-1 grow">
                    <div className="flex-col items-start pt-0 pb-2 px-0 flex-[0_0_auto] flex relative self-stretch w-full">
                        <Label className="text-base leading-6 relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] tracking-[0]">
                            Amount
                        </Label>
                    </div>

                    <Input
                        className="h-14 items-center p-4 bg-[#eae8f2] rounded-lg border-0 text-[#664f96] placeholder:text-[#664f96] [font-family:'Inter',Helvetica] font-normal text-base tracking-[0] leading-6"
                        placeholder="Enter amount"
                        defaultValue=""
                    />
                </div>
            </div>

            <div className="flex items-start justify-center relative self-stretch w-full flex-[0_0_auto]">
                <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms] flex flex-wrap max-w-[480px] items-start gap-[12px_12px] px-4 py-3 flex-1 grow justify-center relative">
                    {amountOptions.slice(0, 2).map((amount, index) => (
                        <Button
                            key={amount.value}
                            variant="outline"
                            className="flex max-w-[480px] w-[218px] h-10 items-center justify-center px-4 py-0 relative bg-[#eae8f2] rounded-lg border-0 hover:bg-[#d6d1e8] transition-colors"
                            onClick={() => setSelectedAmount(amount.value)}
                        >
                            <span className="mt-[-1.00px] font-bold text-[#1e1e2f] text-center whitespace-nowrap relative [font-family:'Inter',Helvetica] text-sm tracking-[0] leading-[21px]">
                                {amount.label}
                            </span>
                        </Button>
                    ))}
                </div>
            </div>

            <div className="flex items-start justify-center relative self-stretch w-full flex-[0_0_auto]">
                <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms] flex flex-wrap max-w-[480px] items-start gap-[12px_12px] px-4 py-3 flex-1 grow justify-center relative">
                    {amountOptions.slice(2, 4).map((amount, index) => (
                        <Button
                            key={amount.value}
                            variant="outline"
                            className="flex max-w-[480px] w-[217px] h-10 items-center justify-center px-4 py-0 relative bg-[#eae8f2] rounded-lg border-0 hover:bg-[#d6d1e8] transition-colors"
                            onClick={() => setSelectedAmount(amount.value)}
                        >
                            <span className="mt-[-1.00px] font-bold text-[#1e1e2f] text-center whitespace-nowrap relative [font-family:'Inter',Helvetica] text-sm tracking-[0] leading-[21px]">
                                {amount.label}
                            </span>
                        </Button>
                    ))}
                </div>
            </div>

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:800ms] flex flex-col items-start pt-5 pb-3 px-4 relative self-stretch w-full flex-[0_0_auto]">
                <h2 className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-bold text-[#e8edf5] text-[22px] tracking-[0] leading-7">
                    Payment Method
                </h2>
            </div>

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1000ms] flex flex-wrap items-start gap-[12px_12px] p-4 relative self-stretch w-full flex-[0_0_auto]">
                <ToggleGroup
                    type="single"
                    value={paymentMethod}
                    onValueChange={setPaymentMethod}
                    className="flex flex-wrap gap-[12px]"
                >
                    {paymentMethods.map((method) => (
                        <ToggleGroupItem
                            key={method.value}
                            value={method.value}
                            className="inline-flex h-11 items-center px-4 py-0 flex-[0_0_auto] rounded-lg border border-solid border-[#d6d1e8] justify-center relative bg-transparent hover:bg-[#d6d1e8] data-[state=on]:bg-[#723ae8] data-[state=on]:border-[#723ae8] transition-colors"
                        >
                            <span className="w-fit text-sm leading-[21px] whitespace-nowrap relative [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] tracking-[0] data-[state=on]:text-white">
                                {method.label}
                            </span>
                        </ToggleGroupItem>
                    ))}
                </ToggleGroup>
            </div>

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1200ms] inline-flex flex-wrap max-w-[480px] items-end gap-[16px_16px] px-4 py-3 relative flex-[0_0_auto]">
                <div className="flex flex-col min-w-40 items-start relative flex-1 grow">
                    <div className="flex-col items-start pt-0 pb-2 px-0 flex-[0_0_auto] flex relative self-stretch w-full">
                        <Label className="self-stretch mt-[-1.00px] text-base leading-6 relative [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] tracking-[0]">
                            Card Number
                        </Label>
                    </div>

                    <Input
                        className="h-14 items-center p-4 bg-[#eae8f2] rounded-lg border-0 text-[#664f96] placeholder:text-[#664f96] [font-family:'Inter',Helvetica] font-normal text-base tracking-[0] leading-6"
                        placeholder="Enter card number"
                        defaultValue=""
                    />
                </div>
            </div>

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1400ms] inline-flex flex-wrap max-w-[480px] items-end gap-[16px_16px] px-4 py-3 relative flex-[0_0_auto]">
                <div className="flex flex-col min-w-40 items-start relative flex-1 grow">
                    <div className="flex-col items-start pt-0 pb-2 px-0 flex-[0_0_auto] flex relative self-stretch w-full">
                        <Label className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] text-base tracking-[0] leading-6">
                            Expiry Date
                        </Label>
                    </div>

                    <Input
                        className="h-14 items-center p-4 bg-[#eae8f2] rounded-lg border-0 text-[#664f96] placeholder:text-[#664f96] [font-family:'Inter',Helvetica] font-normal text-base tracking-[0] leading-6"
                        placeholder="MM/YY"
                        defaultValue=""
                    />
                </div>

                <div className="flex flex-col min-w-40 items-start relative flex-1 grow">
                    <div className="flex-col items-start pt-0 pb-2 px-0 flex-[0_0_auto] flex relative self-stretch w-full">
                        <Label className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] text-base tracking-[0] leading-6">
                            CVV
                        </Label>
                    </div>

                    <Input
                        className="h-14 items-center p-4 bg-[#eae8f2] rounded-lg border-0 text-[#664f96] placeholder:text-[#664f96] [font-family:'Inter',Helvetica] font-normal text-base tracking-[0] leading-6"
                        placeholder="Enter CVV"
                        defaultValue=""
                    />
                </div>
            </div>

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1600ms] inline-flex flex-wrap max-w-[480px] items-end gap-[16px_16px] px-4 py-3 relative flex-[0_0_auto]">
                <div className="flex flex-col min-w-40 items-start relative flex-1 grow">
                    <div className="flex-col items-start pt-0 pb-2 px-0 flex-[0_0_auto] flex relative self-stretch w-full">
                        <Label className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] text-base tracking-[0] leading-6">
                            Cardholder Name
                        </Label>
                    </div>

                    <Input
                        className="h-14 items-center p-4 bg-[#eae8f2] rounded-lg border-0 text-[#664f96] placeholder:text-[#664f96] [font-family:'Inter',Helvetica] font-normal text-base tracking-[0] leading-6"
                        placeholder="Enter cardholder name"
                        defaultValue=""
                    />
                </div>
            </div>

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1800ms] flex flex-col items-start pt-1 pb-3 px-4 relative self-stretch w-full flex-[0_0_auto]">
                <p className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-normal text-[#664f96] text-sm tracking-[0] leading-[21px]">
                    Your card details are encrypted and securely processed.
                </p>
            </div>

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:2000ms] flex flex-col items-start pt-1 pb-3 px-4 relative self-stretch w-full flex-[0_0_auto]">
                <p className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-normal text-[#664f96] text-sm tracking-[0] leading-[21px]">
                    Transaction Fee: None
                </p>
            </div>

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:2200ms] flex flex-col items-start pt-1 pb-3 px-4 relative self-stretch w-full flex-[0_0_auto]">
                <p className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-normal text-[#664f96] text-sm tracking-[0] leading-[21px]">
                    Deposit Limits: R 20 - R 100 , 000
                </p>
            </div>

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:2400ms] flex items-start justify-center px-4 py-3 relative self-stretch w-full flex-[0_0_auto]">
                <Button className="inline-flex min-w-[84px] max-w-[480px] h-10 items-center justify-center px-4 py-0 relative flex-[0_0_auto] bg-[#723ae8] rounded-lg hover:bg-[#5f2bc4] transition-colors">
                    <span className="font-bold text-[#1e1e2f] text-center whitespace-nowrap relative mt-[-1.00px] [font-family:'Inter',Helvetica] text-sm tracking-[0] leading-[21px]">
                        Deposit
                    </span>
                </Button>
            </div>
        </div>
    );
};

import { ChevronLeft, ChevronRight } from "lucide-react";
import React, { useState, useMemo } from "react";
import { Button } from "@/components/ui/button";

const dayHeaders = ["S", "M", "T", "W", "T", "F", "S"];

const monthNames = [
  "January", "February", "March", "April", "May", "June",
  "July", "August", "September", "October", "November", "December"
];

const getDaysInMonth = (year, month) => {
  return new Date(year, month + 1, 0).getDate();
};

const getFirstDayOfMonth = (year, month) => {
  return new Date(year, month, 1).getDay();
};

const generateCalendarDays = (year, month, selectedDate) => {
  const daysInMonth = getDaysInMonth(year, month);
  const firstDay = getFirstDayOfMonth(year, month);
  const today = new Date();
  const isCurrentMonth = today.getFullYear() === year && today.getMonth() === month;
  const todayDate = today.getDate();

  const days = [];

  // Add empty cells for days before the first day of the month
  for (let i = 0; i < firstDay; i++) {
    days.push(null);
  }

  // Add all days of the month
  for (let day = 1; day <= daysInMonth; day++) {
    const isToday = isCurrentMonth && day === todayDate;
    const isSelected = selectedDate &&
      selectedDate.getFullYear() === year &&
      selectedDate.getMonth() === month &&
      selectedDate.getDate() === day;

    days.push({
      day,
      isToday,
      isSelected
    });
  }

  return days;
};

export const CalendarViewSection = () => {
  const today = new Date();
  const [currentDate, setCurrentDate] = useState(today);
  const [selectedDate, setSelectedDate] = useState(today);

  const currentMonth = currentDate.getMonth();
  const currentYear = currentDate.getFullYear();
  const nextMonth = currentMonth === 11 ? 0 : currentMonth + 1;
  const nextMonthYear = currentMonth === 11 ? currentYear + 1 : currentYear;

  const currentMonthDays = useMemo(() =>
    generateCalendarDays(currentYear, currentMonth, selectedDate),
    [currentYear, currentMonth, selectedDate]
  );

  const nextMonthDays = useMemo(() =>
    generateCalendarDays(nextMonthYear, nextMonth, selectedDate),
    [nextMonthYear, nextMonth, selectedDate]
  );

  const handlePrevMonth = () => {
    setCurrentDate(prev => {
      const newDate = new Date(prev);
      newDate.setMonth(prev.getMonth() - 1);
      return newDate;
    });
  };

  const handleNextMonth = () => {
    setCurrentDate(prev => {
      const newDate = new Date(prev);
      newDate.setMonth(prev.getMonth() + 1);
      return newDate;
    });
  };

  const handleDateSelect = (day, month, year) => {
    setSelectedDate(new Date(year, month, day));
  };

  const renderCalendarGrid = (days, month, year, isSecondCalendar = false) => {
    const weeks = [];
    let currentWeek = [];

    days.forEach((day, index) => {
      if (index > 0 && index % 7 === 0) {
        weeks.push(currentWeek);
        currentWeek = [];
      }
      currentWeek.push(day);
    });

    if (currentWeek.length > 0) {
      weeks.push(currentWeek);
    }

    return weeks.map((week, weekIndex) => (
      <div key={`week-${weekIndex}`} className="flex items-start flex-1 w-full grow">
        {week.map((date, dayIndex) => (
          <div
            key={`day-${weekIndex}-${dayIndex}`}
            className="flex-col w-12 h-12 items-center flex"
          >
            {date ? (
              <Button
                variant="ghost"
                onClick={() => handleDateSelect(date.day, month, year)}
                className={`items-center justify-center flex-1 w-full grow rounded-3xl h-auto transition-colors ${date.isSelected
                    ? "bg-[#723ae8] hover:bg-[#5f2bc4]"
                    : date.isToday
                      ? "bg-[#723ae8]/20 hover:bg-[#723ae8]/30"
                      : "hover:bg-[#723ae8]/10"
                  }`}
              >
                <span
                  className={`font-medium text-center w-fit [font-family:'Inter',Helvetica] text-sm tracking-[0] leading-[21px] whitespace-nowrap ${date.isSelected
                      ? "text-white"
                      : date.isToday
                        ? "text-[#723ae8] font-bold"
                        : "text-[#b0bec5]"
                    }`}
                >
                  {date.day}
                </span>
              </Button>
            ) : (
              <div className="flex-1 w-full"></div>
            )}
          </div>
        ))}
        {/* Fill remaining cells if week is incomplete */}
        {Array.from({ length: 7 - week.length }, (_, i) => (
          <div key={`empty-${i}`} className="flex-col w-12 h-12 items-center flex">
            <div className="flex-1 w-full"></div>
          </div>
        ))}
      </div>
    ));
  };

  return (
    <section className="flex flex-wrap items-center justify-center gap-6 p-4 w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
      {/* First Calendar */}
      <div className="flex flex-col min-w-72 max-w-[336px] items-start gap-0.5 flex-1 grow">
        <header className="justify-between p-1 w-full flex items-center">
          <Button
            variant="ghost"
            size="icon"
            className="h-auto p-1 hover:bg-[#723ae8]/10"
            onClick={handlePrevMonth}
          >
            <ChevronLeft className="h-4 w-4 text-[#b0bec5]" />
          </Button>

          <div className="pl-0 pr-10 py-0 flex flex-col items-center flex-1 grow">
            <h2 className="mt-[-1.00px] [font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-base text-center tracking-[0] leading-5">
              {monthNames[currentMonth]} {currentYear}
            </h2>
          </div>
        </header>

        <div className="flex flex-col items-start w-full">
          {/* Day headers */}
          <div className="flex items-start flex-1 w-full grow">
            {dayHeaders.map((day, index) => (
              <div
                key={`header-${index}`}
                className="w-12 h-12 items-center justify-center pt-0 pb-0.5 px-0 flex"
              >
                <div className="w-fit [font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-[13px] tracking-[0] leading-5 whitespace-nowrap">
                  {day}
                </div>
              </div>
            ))}
          </div>

          {/* Calendar grid */}
          {renderCalendarGrid(currentMonthDays, currentMonth, currentYear)}
        </div>
      </div>

      {/* Second Calendar */}
      <div className="flex flex-col min-w-72 max-w-[336px] items-start gap-0.5 flex-1 grow">
        <header className="justify-between p-1 w-full flex items-center">
          <div className="pl-10 pr-0 py-0 flex flex-col items-center flex-1 grow">
            <h2 className="mt-[-1.00px] font-bold text-base text-center leading-5 [font-family:'Inter',Helvetica] text-[#b0bec5] tracking-[0]">
              {monthNames[nextMonth]} {nextMonthYear}
            </h2>
          </div>

          <Button
            variant="ghost"
            size="icon"
            className="h-auto p-1 hover:bg-[#723ae8]/10"
            onClick={handleNextMonth}
          >
            <ChevronRight className="h-4 w-4 text-[#b0bec5]" />
          </Button>
        </header>

        <div className="flex flex-col items-start w-full">
          {/* Day headers */}
          <div className="flex items-start flex-1 w-full grow">
            {dayHeaders.map((day, index) => (
              <div
                key={`next-header-${index}`}
                className="w-12 h-12 items-center justify-center pt-0 pb-0.5 px-0 flex"
              >
                <div className="w-fit [font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-[13px] tracking-[0] leading-5 whitespace-nowrap">
                  {day}
                </div>
              </div>
            ))}
          </div>

          {/* Calendar grid */}
          {renderCalendarGrid(nextMonthDays, nextMonth, nextMonthYear, true)}
        </div>
      </div>
    </section>
  );
};
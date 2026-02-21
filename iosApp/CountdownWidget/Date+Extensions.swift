import Foundation

extension Date {
    func daysRemaining(until target: Date, excluding excludedDates: [Date]) -> Int {
        let calendar = Calendar.current
        let today = calendar.startOfDay(for: .now)
        let targetDay = calendar.startOfDay(for: target)
        guard targetDay > today else { return 0 }

        var count = 0
        var current = today
        while current < targetDay {
            current = calendar.date(byAdding: .day, value: 1, to: current)!
            let isExcluded = excludedDates.contains {
                calendar.isDate($0, inSameDayAs: current)
            }
            if !isExcluded { count += 1 }
        }
        return count
    }

    var shortDateString: String {
        formatted(date: .abbreviated, time: .omitted)
    }
}

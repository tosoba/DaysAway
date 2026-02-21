import ComposeApp
import Foundation
import SwiftData

@Model
final class CountdownModel {
    typealias ID = String
    @Attribute(.unique) var id: String = UUID().uuidString

    var targetDate: Date
    var targetName: String?
    var excludededDates: [Date]

    init(targetDate: Date, targetName: String?, excludededDates: [Date]) {
        self.targetDate = targetDate
        self.targetName = targetName
        self.excludededDates = excludededDates
    }
}

extension CountdownModel {
    convenience init(_ countdown: Countdown) {
        self.init(
            targetDate: DateTimeExtensionsKt.localDateToNSDate(date: countdown.targetDate),
            targetName: countdown.targetName,
            excludededDates: countdown.excludedDates.map { date in DateTimeExtensionsKt.localDateToNSDate(date: date) }
        )
    }
}

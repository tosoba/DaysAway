import ComposeApp
import Foundation
import SwiftData

@Model
final class CountdownModel {
    typealias ID = String
    @Attribute(.unique) var id: String = UUID().uuidString

    var targetDate: Date
    var targetName: String?
    var excludedDates: [Date]

    init(targetDate: Date, targetName: String?, excludedDates: [Date]) {
        self.targetDate = targetDate
        self.targetName = targetName
        self.excludedDates = excludedDates
    }
}

extension CountdownModel {
    convenience init(_ countdown: Countdown) {
        self.init(
            targetDate: DateTimeExtensionsKt.localDateToNSDate(date: countdown.targetDate),
            targetName: countdown.targetName,
            excludedDates: countdown.excludedDates.map { date in DateTimeExtensionsKt.localDateToNSDate(date: date) }
        )
    }
}

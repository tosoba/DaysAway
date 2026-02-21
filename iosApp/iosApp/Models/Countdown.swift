import Foundation
import SwiftData

@Model
final class Countdown {
    typealias ID = String
    @Attribute(.unique) var id: String = UUID().uuidString

    var targetDate: Date
    var targetName: String?
    var excludededDates: [Date]

    init(targetDate: Date, targetName: String? = nil, excludededDates: [Date] = []) {
        self.targetDate = targetDate
        self.targetName = targetName
        self.excludededDates = excludededDates
    }
}

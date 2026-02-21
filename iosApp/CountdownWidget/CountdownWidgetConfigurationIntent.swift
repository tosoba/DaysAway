import AppIntents
import SwiftData
import WidgetKit

struct CountdownWidgetConfigurationIntent: WidgetConfigurationIntent {
    static let title: LocalizedStringResource = "Select a countdown"
    static let description = IntentDescription("Choose which countdown to display.")

    @Parameter(title: "Countdown")
    var countdown: CountdownEntity?
}

struct CountdownEntity: AppEntity {
    let id: String
    let name: String
    let targetDate: Date

    static let typeDisplayRepresentation = TypeDisplayRepresentation(name: "Countdown")
    static let defaultQuery = CountdownEntityQuery()

    var displayRepresentation: DisplayRepresentation {
        DisplayRepresentation(title: "\(name)")
    }
}

struct CountdownEntityQuery: EntityQuery {
    func entities(for identifiers: [String]) async throws -> [CountdownEntity] {
        fetchAll().filter { identifiers.contains($0.id) }
    }

    func suggestedEntities() async throws -> [CountdownEntity] {
        fetchAll()
    }

    private func fetchAll() -> [CountdownEntity] {
        let container = ModelContainer.shared
        let context = ModelContext(container)
        let all = try? context.fetch(FetchDescriptor<CountdownModel>(sortBy: [SortDescriptor(\.targetDate)]))
        return (all ?? []).map {
            CountdownEntity(
                id: $0.id,
                name: $0.targetName ?? $0.targetDate.shortDateString,
                targetDate: $0.targetDate
            )
        }
    }
}

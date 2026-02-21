import AppIntents
import SwiftData
import SwiftUI
import WidgetKit

@main
struct CountdownWidget: Widget {
    let kind = "CountdownWidget"

    var body: some WidgetConfiguration {
        AppIntentConfiguration(
            kind: kind,
            intent: CountdownWidgetConfigurationIntent.self,
            provider: CountdownTimelineProvider()
        ) { entry in
            CountdownWidgetView(entry: entry)
        }
        .configurationDisplayName("DaysAway")
        .description("Counts down to your selected event.")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}

struct CountdownWidgetView: View {
    let entry: CountdownEntry

    var body: some View {
        VStack(alignment: .center, spacing: 4) {
            switch entry.content {
            case .unconfigured:
                Text("Configure widget to select a countdown.")
                    .font(.subheadline)
                    .multilineTextAlignment(.center)
            case let .countdown(daysRemaining, targetName, targetDate, wasReached):
                if !wasReached {
                    Text("\(daysRemaining) \(daysRemaining == 1 ? "day" : "days")")
                        .font(.title2).bold()
                    Text("remaining until")
                        .font(.caption)
                    Text("\(targetName ?? targetDate.shortDateString).")
                        .font(.subheadline)
                } else if let name = targetName, !name.isEmpty {
                    Text(name)
                        .font(.subheadline)
                    Text("was reached on")
                        .font(.caption)
                    Text("\(targetDate.shortDateString).")
                        .font(.subheadline)
                } else {
                    Text(targetDate.shortDateString)
                        .font(.subheadline)
                    Text("was reached")
                        .font(.caption)
                }
            }
        }
        .multilineTextAlignment(.center)
        .padding()
        .containerBackground(.fill.tertiary, for: .widget)
    }
}

enum CountdownEntryContent {
    case unconfigured
    case countdown(daysRemaining: Int, targetName: String?, targetDate: Date, wasReached: Bool)
}

struct CountdownEntry: TimelineEntry {
    let date: Date
    let content: CountdownEntryContent
}

struct CountdownTimelineProvider: AppIntentTimelineProvider {
    typealias Intent = CountdownWidgetConfigurationIntent
    typealias Entry = CountdownEntry
    
    private static let placeholderEntry = CountdownEntry(
        date: .now,
        content: .countdown(daysRemaining: 42, targetName: "My Event", targetDate: .now, wasReached: false)
    )

    func snapshot(for configuration: CountdownWidgetConfigurationIntent, in _: Context) async -> CountdownEntry {
        guard configuration.countdown != nil else { return Self.placeholderEntry }
        return makeEntry(for: configuration)
    }

    func timeline(for configuration: CountdownWidgetConfigurationIntent, in _: Context) async -> Timeline<CountdownEntry> {
        let entry = makeEntry(for: configuration)
        let midnight = Calendar.current.nextDate(
            after: .now,
            matching: DateComponents(hour: 0, minute: 0, second: 0),
            matchingPolicy: .nextTime
        )!
        return Timeline(entries: [entry], policy: .after(midnight))
    }

    func placeholder(in _: Context) -> CountdownEntry {
        Self.placeholderEntry
    }

    private func makeEntry(for configuration: CountdownWidgetConfigurationIntent) -> CountdownEntry {
        guard let selected = configuration.countdown else {
            return CountdownEntry(date: .now, content: .unconfigured)
        }

        let context = ModelContext(ModelContainer.shared)
        let id = selected.id
        let model = try? context.fetch(
            FetchDescriptor<CountdownModel>(predicate: #Predicate { $0.id == id })
        ).first

        let excludedDates = model?.excludedDates ?? []
        let days = Date.now.daysRemaining(until: selected.targetDate, excluding: excludedDates)

        return CountdownEntry(
            date: .now,
            content: .countdown(
                daysRemaining: days,
                targetName: selected.name,
                targetDate: selected.targetDate,
                wasReached: days == 0
            )
        )
    }
}

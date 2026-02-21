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
        .configurationDisplayName("Days Away")
        .description("Counts down to your selected event.")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}

struct CountdownWidgetView: View {
    let entry: CountdownEntry

    var body: some View {
        VStack(alignment: .center, spacing: 4) {
            if !entry.wasReached {
                Text("\(entry.daysRemaining) \(entry.daysRemaining == 1 ? "day" : "days")")
                    .font(.title2).bold()
                Text("remaining until")
                    .font(.caption)
                Text("\(entry.targetName ?? entry.targetDate.shortDateString).")
                    .font(.subheadline)
            } else if let name = entry.targetName, !name.isEmpty {
                Text(name)
                    .font(.subheadline)
                Text("was reached on")
                    .font(.caption)
                Text("\(entry.targetDate.shortDateString).")
                    .font(.subheadline)
            } else {
                Text(entry.targetDate.shortDateString)
                    .font(.subheadline)
                Text("was reached")
                    .font(.caption)
            }
        }
        .multilineTextAlignment(.center)
        .padding()
        .containerBackground(.fill.tertiary, for: .widget)
    }
}

struct CountdownEntry: TimelineEntry {
    let date: Date
    let daysRemaining: Int
    let targetName: String?
    let targetDate: Date
    let wasReached: Bool
}

struct CountdownTimelineProvider: AppIntentTimelineProvider {
    typealias Intent = CountdownWidgetConfigurationIntent
    typealias Entry = CountdownEntry

    func placeholder(in _: Context) -> CountdownEntry {
        CountdownEntry(date: .now, daysRemaining: 42, targetName: "My Event", targetDate: .now, wasReached: false)
    }

    func snapshot(for configuration: CountdownWidgetConfigurationIntent, in _: Context) async -> CountdownEntry {
        makeEntry(for: configuration)
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

    private func makeEntry(for configuration: CountdownWidgetConfigurationIntent) -> CountdownEntry {
        guard let selected = configuration.countdown else {
            return CountdownEntry(date: .now, daysRemaining: 0, targetName: nil, targetDate: .now, wasReached: true)
        }

        let container = ModelContainer.shared
        let context = ModelContext(container)
        let id = selected.id
        let model = try? context.fetch(
            FetchDescriptor<CountdownModel>(predicate: #Predicate { $0.id == id })
        ).first

        let excludedDates = model?.excludedDates ?? []
        let days = Date.now.daysRemaining(until: selected.targetDate, excluding: excludedDates)

        return CountdownEntry(
            date: .now,
            daysRemaining: days,
            targetName: selected.name,
            targetDate: selected.targetDate,
            wasReached: days == 0
        )
    }
}

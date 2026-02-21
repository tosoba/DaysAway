import SwiftData
import SwiftUI

@main
struct iOSApp: App {
    private let container: ModelContainer = {
        let schema = Schema([CountdownModel.self])
        let config = ModelConfiguration(schema: schema, isStoredInMemoryOnly: false)
        return try! ModelContainer(for: schema, configurations: [config])
    }()

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
        .modelContainer(container)
    }
}

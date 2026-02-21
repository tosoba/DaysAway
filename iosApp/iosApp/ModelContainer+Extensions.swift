import Foundation
import SwiftData

extension ModelContainer {
    static var shared: ModelContainer = {
        let schema = Schema([CountdownModel.self])
        let config = ModelConfiguration(
            schema: schema,
            url: FileManager.default
                .containerURL(forSecurityApplicationGroupIdentifier: "group.com.trm.daysaway")!
                .appending(path: "daysaway.store")
        )
        return try! ModelContainer(for: schema, configurations: [config])
    }()
}

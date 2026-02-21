import ComposeApp
import SwiftData
import SwiftUI
import UIKit

struct ComposeView: UIViewControllerRepresentable {
    @Query(sort: \CountdownModel.targetDate) private var countdowns: [CountdownModel]

    @StateObject private var holder: ComposeAppState

    init(onCountdownConfirmClick: @escaping (Countdown) -> Void) {
        _holder = StateObject(wrappedValue: ComposeAppState(onCountdownConfirmClick: onCountdownConfirmClick))
    }

    func makeUIViewController(context _: Context) -> UIViewController {
        MainViewControllerKt.mainViewController(state: holder.appState)
    }

    func updateUIViewController(_: UIViewController, context _: Context) {
        holder.appState.countdowns = countdowns.map { Countdown(model: $0) }
    }
}

class ComposeAppState: ObservableObject {
    let appState: AppState

    init(onCountdownConfirmClick: @escaping (Countdown) -> Void) {
        appState = AppState(onCountdownConfirmClick: onCountdownConfirmClick)
    }
}

struct ContentView: View {
    @Environment(\.modelContext) private var context

    var body: some View {
        ComposeView(
            onCountdownConfirmClick: { countdown in
                context.insert(CountdownModel(countdown))
            }
        )
        .ignoresSafeArea()
    }
}

extension Countdown {
    convenience init(model: CountdownModel) {
        self.init(
            targetDate: DateTimeExtensionsKt.nsDateToLocalDate(date: model.targetDate),
            targetName: model.targetName,
            excludedDates: model.excludedDates.map { date in DateTimeExtensionsKt.nsDateToLocalDate(date: date) }
        )
    }
}

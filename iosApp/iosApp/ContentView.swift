import ComposeApp
import SwiftUI
import UIKit

struct ComposeView: UIViewControllerRepresentable {
    @Environment(\.modelContext) private var context

    func makeUIViewController(context _: Context) -> UIViewController {
        MainViewControllerKt.mainViewController(
            onCountdownConfirmClick: { countdown in
                context.insert(CountdownModel(countdown))
            }
        )
    }

    func updateUIViewController(_: UIViewController, context _: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}

package tui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = terminalFactory.createScreen();
        screen.startScreen();

        // Setup della GUI
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);

        // Finestra principale
        BasicWindow window = new BasicWindow("Lanterna TUI Moderna");

        // Layout
        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new GridLayout(2));

        // Etichetta e campo di testo
        mainPanel.addComponent(new Label("Nome:"));
        TextBox nameBox = new TextBox();
        mainPanel.addComponent(nameBox);

        // Pulsante
        Button button = new Button("Saluta", () -> {
            MessageDialog.showMessageDialog(gui, "Saluto", "Ciao, " + nameBox.getText() + "!", MessageDialogButton.OK);
        });

        // Spaziatura
        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1))); // Spacer
        mainPanel.addComponent(button);

        // Aggiunta al contenitore e visualizzazione
        window.setComponent(mainPanel);
        gui.addWindowAndWait(window);
    }
}

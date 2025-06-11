package presentation;

import java.awt.*;
import javax.swing.*;

public class MainMenuPanel extends PoobkemonPanel {
    private JButton jugarButton, pokemonsButton, cargarButton, salirButton;
    private JPanel buttonPanel;

    public MainMenuPanel(PoobkemonGUI mainGUI) {
        super(mainGUI);
        initialize();
    }

    @Override
    protected void prepareElements() {
        setLayout(new GridBagLayout());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));

        buttonPanel.add(Box.createHorizontalGlue());
        jugarButton = new JButton("Jugar");
        pokemonsButton = new JButton("Pokemons");
        cargarButton = new JButton("Cargar Partida");
        salirButton = new JButton("Salir");
        
        buttonPanel.add(jugarButton);
        buttonPanel.add(pokemonsButton);
        buttonPanel.add(cargarButton);
        buttonPanel.add(salirButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(mainGUI.getHeight()/4, 0, 0, 0);
        
        add(buttonPanel, gbc);
    }

    @Override
    protected void prepareActions() {
        salirButton.addActionListener(e -> mainGUI.confirmarSalida());
        pokemonsButton.addActionListener(e -> mainGUI.showPokemonList());
        cargarButton.addActionListener(e -> mainGUI.cargarPartida());
        jugarButton.addActionListener(e -> {
            String[] options = {"Modo Normal", "Modo Supervivencia"};
            int selectedOption = JOptionPane.showOptionDialog(
                mainGUI,
                "Selecciona el modo de juego:",
                "POOBkemon - Modo de Juego",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            if (selectedOption != -1) {
                int modoJuego = selectedOption + 1;

                int confirmacion = JOptionPane.showConfirmDialog(
                    mainGUI,
                    "¿Estás seguro de iniciar el juego en " + 
                    (modoJuego == 1 ? "Modo Normal" : "Modo Supervivencia") + "?",
                    "Confirmar modo de juego",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    mainGUI.startGame(modoJuego);
                }
            }
        });
    }
}
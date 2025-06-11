package presentation;

import java.awt.*;
import javax.swing.*;


public class BattleTypePanel extends PoobkemonPanel {
    private JButton pvpButton, pvmButton, mvmButton, volverButton;
    private JPanel buttonPanel;
    private JLabel titleLabel;

    public BattleTypePanel(PoobkemonGUI mainGUI) {
        super(mainGUI);
        initialize();
    }

    @Override
    protected void prepareElements() {
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Selecciona el tipo de enfrentamiento", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 20));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        pvpButton = new JButton("Jugador vs Jugador (PvP)");
        pvpButton.setFont(new Font("Arial", Font.BOLD, 16));
        
        pvmButton = new JButton("Jugador vs Máquina (PvM)");
        pvmButton.setFont(new Font("Arial", Font.BOLD, 16));
        
        mvmButton = new JButton("Máquina vs Máquina (MvM)");
        mvmButton.setFont(new Font("Arial", Font.BOLD, 16));
        
        volverButton = new JButton("Volver al menú principal");
        volverButton.setFont(new Font("Arial", Font.PLAIN, 14));

        buttonPanel.add(pvpButton);
        buttonPanel.add(pvmButton);
        buttonPanel.add(mvmButton);
        buttonPanel.add(volverButton);

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    protected void prepareActions() {
        pvpButton.addActionListener(e -> {
            mainGUI.setModalidadJuego(PoobkemonGUI.MODALIDAD_PVP);
        });
        
        pvmButton.addActionListener(e -> {
            mainGUI.setModalidadJuego(PoobkemonGUI.MODALIDAD_PVM);
        });
        
        mvmButton.addActionListener(e -> {
            mainGUI.setModalidadJuego(PoobkemonGUI.MODALIDAD_MVM);
        });
        
        volverButton.addActionListener(e -> {
            mainGUI.showMainMenu();
        });
    }
}
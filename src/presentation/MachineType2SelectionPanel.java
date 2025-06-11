package presentation;

import java.awt.*;
import javax.swing.*;

public class MachineType2SelectionPanel extends PoobkemonPanel {
    private JLabel titleLabel;
    private JPanel buttonPanel;
    private JButton attackingButton, defensiveButton, changingButton, expertButton, backButton;

    public MachineType2SelectionPanel(PoobkemonGUI mainGUI) {
        super(mainGUI);
        initialize();
    }

    @Override
    protected void prepareElements() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));

        titleLabel = new JLabel("Selecciona el tipo de CPU 2", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 20));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        attackingButton = new JButton("Ofensivo - Prioriza ataques poderosos");
        attackingButton.setFont(new Font("Arial", Font.BOLD, 16));
        attackingButton.setToolTipText("CPU que busca causar el máximo daño posible");
        
        defensiveButton = new JButton("Defensivo - Enfoque en resistencia");
        defensiveButton.setFont(new Font("Arial", Font.BOLD, 16));
        defensiveButton.setToolTipText("CPU que prioriza curación y movimientos de estado");
        
        changingButton = new JButton("Cambiante - Cambia Pokémon frecuentemente");
        changingButton.setFont(new Font("Arial", Font.BOLD, 16));
        changingButton.setToolTipText("CPU que cambia constantemente buscando ventaja de tipo");
        
        expertButton = new JButton("Experto - Combina todas las estrategias");
        expertButton.setFont(new Font("Arial", Font.BOLD, 16));
        expertButton.setToolTipText("CPU más inteligente que adapta su estrategia");
        
        backButton = new JButton("Volver");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));

        buttonPanel.add(attackingButton);
        buttonPanel.add(defensiveButton);
        buttonPanel.add(changingButton);
        buttonPanel.add(expertButton);
        buttonPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    protected void prepareActions() {
        attackingButton.addActionListener(e -> {
            mainGUI.setTipoMaquina2("attacking");
            mainGUI.startMvMBattleWithTypes();
        });
        
        defensiveButton.addActionListener(e -> {
            mainGUI.setTipoMaquina2("defensive");
            mainGUI.startMvMBattleWithTypes();
        });
        
        changingButton.addActionListener(e -> {
            mainGUI.setTipoMaquina2("changing");
            mainGUI.startMvMBattleWithTypes();
        });
        
        expertButton.addActionListener(e -> {
            mainGUI.setTipoMaquina2("expert");
            mainGUI.startMvMBattleWithTypes();
        });
        
        backButton.addActionListener(e -> mainGUI.showMachineTypeSelection());
    }
}
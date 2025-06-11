package domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class MainGame implements Serializable {
    
    public static final int MODO_NORMAL = 1;
    public static final int MODO_SUPERVIVENCIA = 2;
    
    public static final int MODALIDAD_PVP = 1;
    public static final int MODALIDAD_PVM = 2;
    public static final int MODALIDAD_MVM = 3;
    
    private static Entrenador entrenador1Configurado;
    private static Entrenador entrenador2Configurado;
    
    private transient static Scanner scanner;
    private static ArrayList<Pokemon> pokemonesDisponibles;
    
    private static int modoJuego;
    private static int modalidadJuego;
    private static Pokemon pokemon1Activo;
    private static Pokemon pokemon2Activo;
    private static boolean turnoEntrenador1;
    private static boolean batallaEnCurso = false;
    
    private static String tipoMaquinaSeleccionado = "attacking";
    private static String tipoMaquina1Seleccionado = "attacking";
    private static String tipoMaquina2Seleccionado = "defensive";
    
    /**
     * Constructor privado para evitar instanciación
     */
    private MainGame() {
    }
    
    public static void main(String[] args) {
        iniciarJuego(MODO_NORMAL);
    }
    
    /**
     * Inicia el juego con el modo seleccionado (versión para la interfaz gráfica)
     * @param modoJuego Modo de juego: MODO_NORMAL (1) o MODO_SUPERVIVENCIA (2)
     * @return true si el juego se inició correctamente, false en caso contrario
     */
    public static boolean iniciarJuegoGUI(int modoJuego) {
        try {
            cargarPokemones();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al cargar los Pokémon: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene los Pokémon disponibles
     * @return Lista de Pokémon disponibles
     */
    public static ArrayList<Pokemon> getPokemonesDisponibles() {
        return pokemonesDisponibles;
    }
    
    /**
     * Inicia el juego con el modo seleccionado (versión para la consola)
     * @param modoJuego Modo de juego: MODO_NORMAL (1) o MODO_SUPERVIVENCIA (2)
     */
    public static void iniciarJuego(int modoJuego) {
        try {
            inicializarJuego();
            mostrarBienvenida();
            
            int modalidadJuego = determinarModalidadJuego(modoJuego);
            Entrenador[] entrenadores = crearEntrenadores(modalidadJuego);
            
            configurarEquipos(entrenadores[0], entrenadores[1], modoJuego, modalidadJuego);
            validarEntrenadores(entrenadores[0], entrenadores[1]);
            
            ejecutarBatalla(entrenadores[0], entrenadores[1]);
            
        } catch (Exception e) {
            manejarError(e);
        } finally {
            limpiarRecursos();
        }
    }

    /**
     * Inicializa los recursos necesarios para el juego
     * @throws Exception si hay error al cargar los recursos
     */
    private static void inicializarJuego() throws Exception {
        cargarPokemones();
        scanner = new Scanner(System.in);
    }

    /**
     * Muestra el mensaje de bienvenida
     */
    private static void mostrarBienvenida() {
        System.out.println("¡Bienvenido a POOBkemon!");
    }

    /**
     * Determina la modalidad de juego basada en el modo seleccionado
     * @param modoJuego Modo de juego seleccionado
     * @return La modalidad de juego correspondiente
     */
    private static int determinarModalidadJuego(int modoJuego) {
        return modoJuego == MODO_SUPERVIVENCIA ? 
               MODALIDAD_PVP : seleccionarModalidadJuego(scanner);
    }

    /**
     * Crea los entrenadores según la modalidad de juego
     * @param modalidadJuego La modalidad de juego seleccionada
     * @return Array con los dos entrenadores creados
     */
    private static Entrenador[] crearEntrenadores(int modalidadJuego) {
        Entrenador entrenador1 = null;
        Entrenador entrenador2 = null;
        
        switch (modalidadJuego) {
            case MODALIDAD_PVP:
                entrenador1 = crearEntrenadorHumano("Jugador 1", scanner);
                entrenador2 = crearEntrenadorHumano("Jugador 2", scanner);
                break;
                
            case MODALIDAD_PVM:
                entrenador1 = crearEntrenadorHumano("Jugador", scanner);
                entrenador2 = crearEntrenadorMaquina("CPU");
                break;
                
            case MODALIDAD_MVM:
                entrenador1 = crearEntrenadorMaquina("CPU 1");
                entrenador2 = crearEntrenadorMaquina("CPU 2");
                break;
        }
        
        return new Entrenador[]{entrenador1, entrenador2};
    }

    /**
     * Configura los equipos de los entrenadores según el modo de juego
     * @param entrenador1 Primer entrenador
     * @param entrenador2 Segundo entrenador
     * @param modoJuego Modo de juego
     * @param modalidadJuego Modalidad de juego
     */
    private static void configurarEquipos(Entrenador entrenador1, Entrenador entrenador2, int modoJuego, int modalidadJuego) {
        if (modoJuego == MODO_NORMAL) {
            configurarEquipoNormal(entrenador1, entrenador2, pokemonesDisponibles, 
                                  scanner, modalidadJuego);
        } else {
            configurarEquipoSupervivencia(entrenador1, entrenador2, pokemonesDisponibles);
        }
    }

    /**
     * Valida que los entrenadores tengan Pokémon con movimientos válidos
     * @param entrenador1 Primer entrenador
     * @param entrenador2 Segundo entrenador
     * @throws POOBkemonException si la validación falla
     */
    private static void validarEntrenadores(Entrenador entrenador1, Entrenador entrenador2) throws POOBkemonException {
        verificarMovimientosPokemon(entrenador1.getPokemones());
        verificarMovimientosPokemon(entrenador2.getPokemones());
    }

    /**
     * Ejecuta la batalla entre los dos entrenadores
     * @param entrenador1 Primer entrenador
     * @param entrenador2 Segundo entrenador
     * @throws POOBkemonException 
     */
    private static void ejecutarBatalla(Entrenador entrenador1, Entrenador entrenador2) throws POOBkemonException {
        Batalla batalla = new Batalla(entrenador1, entrenador2, scanner);
        Entrenador ganador = batalla.iniciarBatalla();
        
        mostrarResultadoBatalla(ganador);
    }

    /**
     * Muestra el resultado de la batalla
     * @param ganador El entrenador ganador (puede ser null en caso de empate)
     */
    private static void mostrarResultadoBatalla(Entrenador ganador) {
        if (ganador != null) {
            System.out.println("¡" + ganador.getNombre() + " ha ganado la batalla!");
        } else {
            System.out.println("La batalla ha terminado en empate.");
        }
    }

    /**
     * Maneja los errores que puedan ocurrir durante el juego
     * @param e La excepción ocurrida
     */
    private static void manejarError(Exception e) {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Limpia los recursos utilizados
     */
    private static void limpiarRecursos() {
        if (scanner != null) {
            scanner.close();
        }
    }
    
    /**
     * Inicia una batalla con los entrenadores configurados a través de la interfaz gráfica
     * @param modoJuego Modo de juego: MODO_NORMAL (1) o MODO_SUPERVIVENCIA (2)
     * @param modalidadJuego Modalidad de juego: MODALIDAD_PVP (1), MODALIDAD_PVM (2) o MODALIDAD_MVM (3)
     * @param nombreEntrenador1 Nombre del entrenador 1 (puede ser null para MVM)
     * @param nombreEntrenador2 Nombre del entrenador 2 (puede ser null para PVM o MVM)
     * @return true si la inicialización fue exitosa, false en caso contrario
     */
    public static boolean iniciarBatallaGUI(int modoJuego, int modalidadJuego, String nombreEntrenador1, String nombreEntrenador2) {
        try {
            Entrenador[] entrenadores = crearEntrenadoresBasicos(modoJuego, modalidadJuego, nombreEntrenador1, nombreEntrenador2);
            
            if (entrenadores == null) {
                return false;
            }

            if (modoJuego == MODO_SUPERVIVENCIA) {
                configurarEquiposSupervivencia();
            }

            if (modalidadJuego == MODALIDAD_MVM) {
                return verificarEntrenadores();
            }

            return true;
                    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al iniciar la batalla: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Crea un entrenador humano para la interfaz gráfica
     * @param nombre Nombre del entrenador
     * @return El entrenador creado
     */
    public static EntrenadorPersona crearEntrenadorHumanoGUI(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            nombre = "Jugador";
        }
        
        EntrenadorPersona entrenador = new EntrenadorPersona(nombre);
        return entrenador;
    }
    
    /**
     * Carga los Pokémon disponibles desde el archivo binario
     * @throws Exception si hay error al cargar los Pokémon
     * CÓDIGO GENERADO POR IA
     */
    @SuppressWarnings({ "unchecked", "resource" })
    private static void cargarPokemones() throws Exception {
    	FileInputStream fileIn = new FileInputStream("pokemones.bin");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Object obj = in.readObject();
        
        if (obj instanceof ArrayList<?>) {
            pokemonesDisponibles = (ArrayList<Pokemon>) obj;
        } else {
            throw new ClassCastException("El objeto deserializado no es un ArrayList<Pokemon>");
        }
        
        in.close();
        fileIn.close();
    }
    
    /**
     * Solicita al usuario que seleccione una modalidad de juego.
     * @param scanner Scanner para leer la entrada del usuario.
     * @param modoJuego El modo de juego seleccionado previamente.
     * @return La modalidad de juego seleccionada.
     */
    private static int seleccionarModalidadJuego(Scanner scanner) {
        int modalidadJuego = 0;

        while (modalidadJuego < 1 || modalidadJuego > 3) {
            System.out.println("\nSelecciona la modalidad de juego:");
            System.out.println("1. Jugador vs Jugador (PvP)");
            System.out.println("2. Jugador vs Máquina (PvM)");
            System.out.println("3. Máquina vs Máquina (MvM)");
            System.out.print("Opción: ");
            
            try {
                modalidadJuego = scanner.nextInt();
                scanner.nextLine();
                
                if (modalidadJuego < 1 || modalidadJuego > 3) {
                    System.out.println("Opción no válida. Por favor, selecciona entre 1 y 3.");
                }
            } catch (Exception e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                scanner.nextLine();
            }
        }
        
        return modalidadJuego;
    }
    
    /**
     * Crea un entrenador humano.
     * @param defaultName Nombre predeterminado.
     * @param scanner Scanner para leer la entrada del usuario.
     * @return El entrenador humano creado.
     */
    private static EntrenadorPersona crearEntrenadorHumano(String defaultName, Scanner scanner) {
        System.out.println("\nConfiguración de " + defaultName);
        System.out.print("Ingresa el nombre del entrenador (o presiona Enter para usar '" + defaultName + "'): ");
        String nombre = scanner.nextLine().trim();
        
        if (nombre.isEmpty()) {
            nombre = defaultName;
        }
        
        EntrenadorPersona entrenador = new EntrenadorPersona(nombre);
        entrenador.setScanner(scanner);
        
        return entrenador;
    }
    
    /**
     * Crea un entrenador controlado por la máquina.
     * @param nombre Nombre del entrenador.
     * @return El entrenador máquina creado.
     */
    public static EntrenadorMaquina crearEntrenadorMaquina(String nombre) {
        Random random = new Random();
        boolean esOfensivo = random.nextBoolean();
        
        if (esOfensivo) {
            return new AttackingTrainer(nombre);
        } else {
            return new DefensiveTrainer(nombre);
        }
    }
    
    /**
     * Configura los equipos para el modo normal.
     * @param entrenador1 Primer entrenador.
     * @param entrenador2 Segundo entrenador.
     * @param pokemonesDisponibles Lista de todos los Pokémon disponibles.
     * @param scanner Scanner para leer la entrada del usuario.
     * @param modalidadJuego La modalidad de juego seleccionada.
     */
    private static void configurarEquipoNormal(Entrenador entrenador1, Entrenador entrenador2, ArrayList<Pokemon> pokemonesDisponibles, Scanner scanner, int modalidadJuego) {
        if (entrenador1 instanceof EntrenadorPersona) {
            seleccionarEquipoManual(entrenador1, pokemonesDisponibles, scanner, 6);
            agregarItemsManual(entrenador1, scanner);
        } else {
            seleccionarEquipoAutomatico(entrenador1, pokemonesDisponibles, 6);
            agregarItemsAutomatico(entrenador1);
        }

        if (entrenador2 instanceof EntrenadorPersona) {
            seleccionarEquipoManual(entrenador2, pokemonesDisponibles, scanner, 6);
            agregarItemsManual(entrenador2, scanner);
        } else {
            seleccionarEquipoAutomatico(entrenador2, pokemonesDisponibles, 6);
            agregarItemsAutomatico(entrenador2);
        }
    }
    
    /**
     * Configura los equipos para el modo supervivencia.
     * @param entrenador1 Primer entrenador.
     * @param entrenador2 Segundo entrenador.
     * @param pokemonesDisponibles Lista de todos los Pokémon disponibles.
     */
    private static void configurarEquipoSupervivencia(Entrenador entrenador1, Entrenador entrenador2, ArrayList<Pokemon> pokemonesDisponibles) {
        seleccionarEquipoAutomatico(entrenador1, pokemonesDisponibles, 6);
        seleccionarEquipoAutomatico(entrenador2, pokemonesDisponibles, 6);
        
        System.out.println("\n¡Modo Supervivencia! Cada entrenador recibe 6 Pokémon aleatorios y ningún ítem.");

        System.out.println("\nEquipo de " + entrenador1.getNombre() + ":");
        for (Pokemon pokemon : entrenador1.getPokemones()) {
            System.out.println("- " + pokemon.getName());
        }
        
        System.out.println("\nEquipo de " + entrenador2.getNombre() + ":");
        for (Pokemon pokemon : entrenador2.getPokemones()) {
            System.out.println("- " + pokemon.getName());
        }
    }
    
    /**
     * Permite al usuario seleccionar manualmente su equipo de Pokémon.
     * @param entrenador El entrenador que seleccionará su equipo.
     * @param pokemonesDisponibles Lista de todos los Pokémon disponibles.
     * @param scanner Scanner para leer la entrada del usuario.
     * @param cantidadPokemon Cantidad de Pokémon a seleccionar.
     */
    private static void seleccionarEquipoManual(Entrenador entrenador, ArrayList<Pokemon> pokemonesDisponibles, Scanner scanner, int cantidadPokemon) {
        System.out.println("\n" + entrenador.getNombre() + ", elige " + cantidadPokemon + " Pokémon para tu equipo:");
        
        for (int i = 0; i < cantidadPokemon; i++) {
            System.out.println("\nPokémon disponibles:");
            for (int j = 0; j < pokemonesDisponibles.size(); j++) {
                System.out.println((j + 1) + ". " + pokemonesDisponibles.get(j).getName());
            }
            
            System.out.print("Elige el Pokémon #" + (i + 1) + " (1-" + pokemonesDisponibles.size() + "): ");
            int eleccion = 0;
            
            try {
                eleccion = scanner.nextInt() - 1;
                scanner.nextLine();
                
                if (eleccion < 0 || eleccion >= pokemonesDisponibles.size()) {
                    System.out.println("Opción no válida. Se asignará un Pokémon aleatorio.");
                    eleccion = new Random().nextInt(pokemonesDisponibles.size());
                }
            } catch (Exception e) {
                System.out.println("Entrada no válida. Se asignará un Pokémon aleatorio.");
                scanner.nextLine();
                eleccion = new Random().nextInt(pokemonesDisponibles.size());
            }

            Pokemon pokemonSeleccionado = new Pokemon(pokemonesDisponibles.get(eleccion));
            entrenador.agregarPokemon(pokemonSeleccionado);
            System.out.println("¡" + pokemonesDisponibles.get(eleccion).getName() + " ha sido añadido a tu equipo!");
        }
        
        System.out.println("\nEquipo de " + entrenador.getNombre() + " completado:");
        for (Pokemon pokemon : entrenador.getPokemones()) {
            System.out.println("- " + pokemon.getName());
        }
    }
    
    /**
     * Selecciona automáticamente un equipo de Pokémon para un entrenador (método público)
     * @param entrenador El entrenador que recibirá el equipo.
     * @param pokemonesDisponibles Lista de todos los Pokémon disponibles.
     * @param cantidadPokemon Cantidad de Pokémon a seleccionar.
     */
    public static void seleccionarEquipoAutomatico(Entrenador entrenador, ArrayList<Pokemon> pokemonesDisponibles, int cantidadPokemon) {
        Random random = new Random();
        ArrayList<Integer> indicesElegidos = new ArrayList<>();
        
        for (int i = 0; i < cantidadPokemon; i++) {
            int indiceAleatorio;
            do {
                indiceAleatorio = random.nextInt(pokemonesDisponibles.size());
            } while (indicesElegidos.contains(indiceAleatorio));
            
            indicesElegidos.add(indiceAleatorio);
            Pokemon pokemonSeleccionado = new Pokemon(pokemonesDisponibles.get(indiceAleatorio));
            entrenador.agregarPokemon(pokemonSeleccionado);;
        }
    }
    
    /**
     * Permite al usuario seleccionar manualmente los ítems para su entrenador.
     * @param entrenador El entrenador que seleccionará sus ítems.
     * @param scanner Scanner para leer la entrada del usuario.
     */
    private static void agregarItemsManual(Entrenador entrenador, Scanner scanner) {
        System.out.println("\n" + entrenador.getNombre() + ", elige los ítems para tu entrenador:");
        
        boolean seguirAgregando = true;
        int totalItems = 0;
        
        while (seguirAgregando && totalItems < 7) {
            System.out.println("\nÍtems disponibles:");
            System.out.println("1. Poción Normal (restaura 20 PS)");
            System.out.println("2. Super Poción (restaura 50 PS)");
            System.out.println("3. Hiper Poción (restaura 100 PS)");
            System.out.println("4. Revivir (revive a un Pokémon debilitado con la mitad de sus PS)");
            System.out.println("5. No agregar más ítems");
            
            System.out.print("Elige un ítem (1-5): ");
            int eleccion = 0;
            
            try {
                eleccion = scanner.nextInt();
                scanner.nextLine();
                
                switch (eleccion) {
                    case 1:
                        entrenador.agregarItem(new NormalPotion());
                        System.out.println("¡Poción Normal agregada!");
                        totalItems++;
                        break;
                    case 2:
                        entrenador.agregarItem(new SuperPotion());
                        System.out.println("¡Super Poción agregada!");
                        totalItems++;
                        break;
                    case 3:
                        entrenador.agregarItem(new HyperPotion());
                        System.out.println("¡Hiper Poción agregada!");
                        totalItems++;
                        break;
                    case 4:
                        entrenador.agregarItem(new Revive());
                        System.out.println("¡Revivir agregado!");
                        totalItems++;
                        break;
                    case 5:
                        seguirAgregando = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, elige entre 1 y 5.");
                        break;
                }
                
                if (totalItems >= 7) {
                    System.out.println("Has alcanzado el límite de 7 ítems.");
                    seguirAgregando = false;
                }
                
            } catch (Exception e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                scanner.nextLine();
            }
        }
        
        System.out.println("\nÍtems de " + entrenador.getNombre() + ":");
        if (entrenador.getItems().isEmpty()) {
            System.out.println("No has seleccionado ningún ítem.");
        } else {
            for (Item item : entrenador.getItems()) {
                System.out.println("- " + item.getNombre() + ": " + item.getDescripcion());
            }
        }
    }
    
    /**
     * Agrega automáticamente ítems a un entrenador (método público)
     * @param entrenador El entrenador que recibirá los ítems.
     */
    public static void agregarItemsAutomatico(Entrenador entrenador) {
        Random random = new Random();
        int cantidadItems = random.nextInt(4) + 1;
        
        for (int i = 0; i < cantidadItems; i++) {
            int tipoItem = random.nextInt(4);
            
            switch (tipoItem) {
                case 0:
                    entrenador.agregarItem(new NormalPotion());
                    break;
                case 1:
                    entrenador.agregarItem(new SuperPotion());
                    break;
                case 2:
                    entrenador.agregarItem(new HyperPotion());
                    break;
                case 3:
                    entrenador.agregarItem(new Revive());
                    break;
            }
        }
    }
    
    /**
     * Verifica que todos los Pokémon en la lista tengan movimientos asignados.
     * @param list Lista de Pokémon a verificar.
     * @throws POOBkemonException Si algún Pokémon no tiene movimientos asignados.
     */
    private static void verificarMovimientosPokemon(List<Pokemon> list) throws POOBkemonException {
        for (Pokemon pokemon : list) {
            if (pokemon.getMovimientos() == null || pokemon.getMovimientos().isEmpty()) {
                throw new POOBkemonException("¡Error! El Pokémon " + pokemon.getName() + 
                                             " no tiene movimientos asignados. Verifica la carga de datos.");
            }
            for (int i = 0; i < pokemon.getMovimientos().size(); i++) {
                if (pokemon.getMovimientos().get(i) == null) {
                    throw new POOBkemonException("¡Error! El Pokémon " + pokemon.getName() + 
                                                " tiene un movimiento nulo en la posición " + (i + 1) + 
                                                ". Verifica la carga de datos.");
                }
            }
        }
    }
    
    public static Entrenador getEntrenador1() {
        return entrenador1Configurado;
    }

    public static Entrenador getEntrenador2() {
        return entrenador2Configurado;
    }
    
    /**
     * Crea entrenadores sin configurar equipos
     * @param modoJuego Modo de juego
     * @param modalidadJuego Modalidad de juego
     * @param nombreEntrenador1 Nombre del primer entrenador
     * @param nombreEntrenador2 Nombre del segundo entrenador
     * @return Un array con los dos entrenadores creados
     */
    public static Entrenador[] crearEntrenadoresBasicos(int modoJuego, int modalidadJuego, String nombreEntrenador1, String nombreEntrenador2) {
        try {
            if (pokemonesDisponibles == null) {
                cargarPokemones();
            }

            Entrenador entrenador1 = null;
            Entrenador entrenador2 = null;
            
            switch (modalidadJuego) {
                case MODALIDAD_PVP:
                    entrenador1 = crearEntrenadorHumanoGUI(nombreEntrenador1);
                    entrenador2 = crearEntrenadorHumanoGUI(nombreEntrenador2);
                    break;
                    
                case MODALIDAD_PVM:
                    entrenador1 = crearEntrenadorHumanoGUI(nombreEntrenador1);
                    String tipoCPU = obtenerTipoMaquinaDesdeGUI();
                    entrenador2 = crearEntrenadorMaquinaConTipo(nombreEntrenador2 != null ? nombreEntrenador2 : "CPU", tipoCPU);

                    if (modoJuego == MODO_SUPERVIVENCIA) {
                        seleccionarEquipoAutomatico(entrenador2, pokemonesDisponibles, 6);
                        agregarItemsAutomatico(entrenador2);
                    }
                    break;
                    
                case MODALIDAD_MVM:
                    String tipoCPU1 = obtenerTipoMaquina1DesdeGUI();
                    String tipoCPU2 = obtenerTipoMaquina2DesdeGUI();
                    
                    entrenador1 = crearEntrenadorMaquinaConTipo(nombreEntrenador1 != null ? nombreEntrenador1 : "CPU 1", tipoCPU1);
                    entrenador2 = crearEntrenadorMaquinaConTipo(nombreEntrenador2 != null ? nombreEntrenador2 : "CPU 2", tipoCPU2);

                    seleccionarEquipoAutomatico(entrenador1, pokemonesDisponibles, 6);
                    agregarItemsAutomatico(entrenador1);
                    seleccionarEquipoAutomatico(entrenador2, pokemonesDisponibles, 6);
                    agregarItemsAutomatico(entrenador2);
                    break;
            }
            entrenador1Configurado = entrenador1;
            entrenador2Configurado = entrenador2;
            return new Entrenador[] {entrenador1, entrenador2};
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al crear entrenadores: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Configura automáticamente los equipos en modo supervivencia
     * @param modoJuego Modo de juego
     */
    public static void configurarEquiposSupervivencia() {
        try {
            if (entrenador1Configurado != null && entrenador2Configurado != null) {

                if (entrenador1Configurado.getPokemones().isEmpty()) {
                    seleccionarEquipoAutomatico(entrenador1Configurado, pokemonesDisponibles, 6);
                }
                
                if (entrenador2Configurado.getPokemones().isEmpty()) {
                    seleccionarEquipoAutomatico(entrenador2Configurado, pokemonesDisponibles, 6);
                }

                StringBuilder message = new StringBuilder();
                message.append("¡Modo Supervivencia! Cada entrenador recibe 6 Pokémon aleatorios y ningún ítem.\n\n");
                
                message.append("Equipo de ").append(entrenador1Configurado.getNombre()).append(":\n");
                for (Pokemon pokemon : entrenador1Configurado.getPokemones()) {
                    message.append("- ").append(pokemon.getName()).append("\n");
                }
                
                message.append("\nEquipo de ").append(entrenador2Configurado.getNombre()).append(":\n");
                for (Pokemon pokemon : entrenador2Configurado.getPokemones()) {
                    message.append("- ").append(pokemon.getName()).append("\n");
                }
                
                JOptionPane.showMessageDialog(null, message.toString(), 
                    "Modo Supervivencia", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al configurar equipos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Verifica que los entrenadores configurados tengan Pokémon con movimientos válidos
     * @return true si la verificación es exitosa, false si hay errores
     */
    public static boolean verificarEntrenadores() {
        try {
            if (entrenador1Configurado != null && !entrenador1Configurado.getPokemones().isEmpty()) {
                verificarMovimientosPokemon(entrenador1Configurado.getPokemones());
            }
            
            if (entrenador2Configurado != null && !entrenador2Configurado.getPokemones().isEmpty()) {
                verificarMovimientosPokemon(entrenador2Configurado.getPokemones());
            }
            
            return true;
        } catch (POOBkemonException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al verificar Pokémon: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Guarda el estado completo de la partida en un archivo
     * @param file archivo donde guardar la partida
     * @param modoJuegoActual el modo de juego actual (MODO_NORMAL o MODO_SUPERVIVENCIA)
     * @param modalidadJuegoActual la modalidad de juego actual
     * @throws POOBkemonPersistenceException si ocurre un error al guardar
     */
    public static void guardarPartida(File file, int modoJuegoActual, int modalidadJuegoActual) throws POOBkemonPersistenceException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {

            modoJuego = modoJuegoActual;
            modalidadJuego = modalidadJuegoActual;

            out.writeObject(entrenador1Configurado);
            out.writeObject(entrenador2Configurado);
            out.writeInt(modoJuego);
            out.writeInt(modalidadJuego);
            out.writeObject(pokemonesDisponibles);

            out.writeObject(pokemon1Activo);
            out.writeObject(pokemon2Activo);
            out.writeBoolean(turnoEntrenador1);
            out.writeBoolean(batallaEnCurso);
            
        } catch (IOException e) {
            throw new POOBkemonPersistenceException("Error al guardar la partida: " + e.getMessage(), e);
        }
    }
    
    /**
     * Versión simplificada que usa el modo y modalidad actuales
     */
    public static void guardarPartida(File file) throws POOBkemonPersistenceException {
        guardarPartida(file, modoJuego, modalidadJuego);
    }
    
    /**
     * Carga una partida guardada desde un archivo, recuperando todo el estado
     * @param file archivo desde donde cargar la partida
     * @throws POOBkemonPersistenceException si ocurre un error al cargar
     */
    @SuppressWarnings("unchecked")
    public static void cargarPartida(File file) throws POOBkemonPersistenceException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {

            entrenador1Configurado = (Entrenador) in.readObject();
            entrenador2Configurado = (Entrenador) in.readObject();
            modoJuego = in.readInt();
            modalidadJuego = in.readInt();
            pokemonesDisponibles = (ArrayList<Pokemon>) in.readObject();
            
            pokemon1Activo = (Pokemon) in.readObject();
            pokemon2Activo = (Pokemon) in.readObject();
            turnoEntrenador1 = in.readBoolean();
            batallaEnCurso = in.readBoolean();
            
        } catch (IOException | ClassNotFoundException e) {
            throw new POOBkemonPersistenceException("Error al cargar la partida: " + e.getMessage(), e);
        }
    }
      
    public static void setEntrenador1(Entrenador entrenador) {
        entrenador1Configurado = entrenador;
    }
    
    public static void setEntrenador2(Entrenador entrenador) {
        entrenador2Configurado = entrenador;
    }
    
    public static int getModoJuego() {
        return modoJuego;
    }
    
    public static void setModoJuego(int modo) {
        modoJuego = modo;
    }
    
    public static int getModalidadJuego() {
        return modalidadJuego;
    }
    
    public static void setModalidadJuego(int modalidad) {
        modalidadJuego = modalidad;
    }
    
    public static Pokemon getPokemon1Activo() {
        return pokemon1Activo;
    }
    
    public static void setPokemon1Activo(Pokemon pokemon) {
        pokemon1Activo = pokemon;
    }
    
    public static Pokemon getPokemon2Activo() {
        return pokemon2Activo;
    }
    
    public static void setPokemon2Activo(Pokemon pokemon) {
        pokemon2Activo = pokemon;
    }
    
    public static boolean isTurnoEntrenador1() {
        return turnoEntrenador1;
    }
    
    public static void setTurnoEntrenador1(boolean turno) {
        turnoEntrenador1 = turno;
    }
    
    public static boolean isBatallaEnCurso() {
        return batallaEnCurso;
    }
    
    public static void setBatallaEnCurso(boolean enCurso) {
        batallaEnCurso = enCurso;
    }
    
    public static void setTipoMaquinaSeleccionado(String tipo) {
        tipoMaquinaSeleccionado = tipo;
    }

    public static void setTipoMaquina1Seleccionado(String tipo) {
        tipoMaquina1Seleccionado = tipo;
    }

    public static void setTipoMaquina2Seleccionado(String tipo) {
        tipoMaquina2Seleccionado = tipo;
    }

    private static String obtenerTipoMaquinaDesdeGUI() {
        return tipoMaquinaSeleccionado != null ? tipoMaquinaSeleccionado : "attacking";
    }

    private static String obtenerTipoMaquina1DesdeGUI() {
        return tipoMaquina1Seleccionado != null ? tipoMaquina1Seleccionado : "attacking";
    }

    private static String obtenerTipoMaquina2DesdeGUI() {
        return tipoMaquina2Seleccionado != null ? tipoMaquina2Seleccionado : "defensive";
    }
    
    /**
     * Crea un entrenador controlado por la máquina con tipo específico.
     * @param nombre Nombre del entrenador.
     * @param tipo Tipo específico de entrenador ("attacking", "defensive", "changing", "expert")
     * @return El entrenador máquina creado.
     */
    public static EntrenadorMaquina crearEntrenadorMaquinaConTipo(String nombre, String tipo) {
        switch (tipo.toLowerCase()) {
            case "attacking":
                return new AttackingTrainer(nombre);
            case "defensive":
                return new DefensiveTrainer(nombre);
            case "changing":
                return new ChangingTrainer(nombre);
            case "expert":
                return new ExpertTrainer(nombre);
            default:
                return new AttackingTrainer(nombre);
        }
    }  
}
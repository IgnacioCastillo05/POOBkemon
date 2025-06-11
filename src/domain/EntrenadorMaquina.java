package domain;

import java.util.*;

public abstract class EntrenadorMaquina extends Entrenador {

    private String tipoMaquina;
    protected Random random;

    public EntrenadorMaquina(String nombre, String tipoMaquina) {
        super(nombre);
        this.tipoMaquina = tipoMaquina;
        this.random = new Random();
    }
    
    public String getTipoMaquina() {
        return tipoMaquina;
    }

    @Override
    public int elegirAccion(Entrenador usuario, Scanner scanner) throws POOBkemonException {
        Pokemon pokemonActivo = getPrimerPokemonDisponible();
        Pokemon pokemonOponente = usuario.getPrimerPokemonDisponible();

        if (pokemonActivo.getPs() < pokemonActivo.getMaxPs() * 0.2 && tieneMasDeUnPokemonDisponible()) {
            return 2;
        }

        if (!getItems().isEmpty() && deberiaUsarItem(pokemonActivo)) {
            return 3;
        }

        return 1;
    }
    
    @Override
    public Pokemon elegirPokemon() throws POOBkemonException {
        return elegirMejorPokemon();
    }
    
    @Override
    public Movimiento elegirMovimiento(Pokemon pokemon, Scanner scanner) throws POOBkemonException {
        return elegirMovimientoConValidacionPP(pokemon);
    }
    
    @Override
    public Item elegirItem() throws POOBkemonException {
        if (getItems().isEmpty()) {
            throw new POOBkemonException("No hay items disponibles.");
        }
        
        return getItems().get(0);
    }
    
    @Override
    public Pokemon elegirPokemonParaItem() throws POOBkemonException {
        Pokemon pokemonMasDebil = null;
        int menorPorcentajePS = 100;
        
        for (Pokemon pokemon : getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                int porcentajePS = (pokemon.getPs() * 100) / pokemon.getMaxPs();
                if (pokemonMasDebil == null || porcentajePS < menorPorcentajePS) {
                    pokemonMasDebil = pokemon;
                    menorPorcentajePS = porcentajePS;
                }
            }
        }
        
        if (pokemonMasDebil == null) {
            throw new POOBkemonException("No hay Pokémon disponibles.");
        }
        
        return pokemonMasDebil;
    }
    
    @Override
    public boolean decidirHuir(Entrenador usuario, Scanner scanner) throws POOBkemonException {
        return false;
    }
    
    /**
     * Elige el mejor Pokémon según la estrategia del entrenador
     * @return El Pokémon elegido
     */
    protected abstract Pokemon elegirMejorPokemon();
    
    /**
     * Elige el mejor movimiento según la estrategia del entrenador
     * @param pokemon El Pokémon que realizará el movimiento
     * @return El movimiento elegido
     */
    protected abstract Movimiento elegirMejorMovimiento(Pokemon pokemon);
    
    /**
     * Método sobrecargado que recibe solo movimientos con PP
     */
    protected abstract Movimiento elegirMejorMovimiento(Pokemon pokemon, List<Movimiento> movimientosDisponibles);
    
    /**
     * Determina si se debe usar un item en base al estado actual
     * @param pokemon El Pokémon activo
     * @return true si debería usar un item, false en caso contrario
     */
    protected boolean deberiaUsarItem(Pokemon pokemon) {
        return pokemon.getPs() < pokemon.getMaxPs() * 0.3;
    }
    
    /**
     * Verifica si el entrenador tiene más de un Pokémon disponible
     * @return true si tiene más de un Pokémon no debilitado, false en caso contrario
     */
    protected boolean tieneMasDeUnPokemonDisponible() {
        int disponibles = 0;
        for (Pokemon pokemon : getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                disponibles++;
                if (disponibles > 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Validación de PP para todos los entrenadores
     */
    protected boolean validarMovimientoTienePP(Movimiento movimiento) {
        return movimiento != null && movimiento.tienePPDisponibles();
    }

    /**
     * Filtra movimientos que tienen PP disponibles
     */
    protected List<Movimiento> filtrarMovimientosConPP(List<Movimiento> movimientos) {
        List<Movimiento> movimientosDisponibles = new ArrayList<>();
        
        for (Movimiento movimiento : movimientos) {
            if (validarMovimientoTienePP(movimiento)) {
                movimientosDisponibles.add(movimiento);
            }
        }
        
        return movimientosDisponibles;
    }

    /**
     * Verifica si un Pokémon tiene al menos un movimiento con PP
     */
    protected boolean pokemonTieneMovimientosDisponibles(Pokemon pokemon) {
        if (pokemon.getMovimientos() == null || pokemon.getMovimientos().isEmpty()) {
            return false;
        }
        
        for (Movimiento movimiento : pokemon.getMovimientos()) {
            if (validarMovimientoTienePP(movimiento)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Método para elegir movimiento con validación de PP
     */
    protected Movimiento elegirMovimientoConValidacionPP(Pokemon pokemon) {
        List<Movimiento> movimientos = pokemon.getMovimientos();
        
        if (movimientos == null || movimientos.isEmpty()) {
            return null;
        }
        
        List<Movimiento> movimientosDisponibles = filtrarMovimientosConPP(movimientos);
        
        if (movimientosDisponibles.isEmpty()) {
            return new MovimientoForcejeo();
        }
        return elegirMejorMovimiento(pokemon, movimientosDisponibles);
    }
}
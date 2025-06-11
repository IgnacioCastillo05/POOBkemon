
import domain.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
//import java.util.ArrayList;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;

public class MovimientoSerializer {
    
    public static void main(String[] args) {

        Map<String, Movimiento> movimientos = new HashMap<>();
            
            // Crear los tipos
            Tipo tipoBicho = new Tipo("Bug");
            Tipo tipoSiniestro = new Tipo("Dark");
            Tipo tipoDragon = new Tipo("Dragon");
            Tipo tipoElectrico = new Tipo("Electric");
            Tipo tipoHada = new Tipo("Fairy");
            Tipo tipoLucha = new Tipo("Fighting");
            Tipo tipoFuego = new Tipo("Fire");
            Tipo tipoVolador = new Tipo("Flying");
            Tipo tipoFantasma = new Tipo("Ghost");
            Tipo tipoPlanta = new Tipo("Grass");
            Tipo tipoTierra = new Tipo("Ground");
            Tipo tipoHielo = new Tipo("Ice");
            Tipo tipoNormal = new Tipo("Normal");
            Tipo tipoVeneno = new Tipo("Poison");
            Tipo tipoPsiquico = new Tipo("Psychic");
            Tipo tipoRoca = new Tipo("Rock");
            Tipo tipoAcero = new Tipo("Steel");
            Tipo tipoAgua = new Tipo("Water");
            
            // Bug types movements
            Movimiento estoicismo = new MovimientoEspecial("Estoicisimo", tipoBicho, 50, 100, 20, 20, false, "especial", "Reduce el ataque especial del oponente");
            movimientos.put(estoicismo.getNombre(), estoicismo);
            
            Movimiento picadura = new MovimientoFisico("Picadura", tipoBicho, 60, 100, 20, 20, false, "físico", "daño al oponente");
            movimientos.put(picadura.getNombre(), picadura);
            
            Movimiento disparoDemora = new MovimientoEstado("Disparo Demora", tipoBicho, 0, 95, 40, 40, false, "estado", "Reduce la velocidad del oponente");
            movimientos.put(disparoDemora.getNombre(), disparoDemora);
            
            // Dark types movements
            Movimiento triturar = new MovimientoFisico("Triturar", tipoSiniestro, 80, 100, 15, 15, false, "físico", "Reduce la defensa del oponente");
            movimientos.put(triturar.getNombre(), triturar);
            
            Movimiento pulsoUmbrio = new MovimientoEspecial("Pulso Umbrio", tipoSiniestro, 80, 100, 15, 15, false, "especial", "daño al oponente");
            movimientos.put(pulsoUmbrio.getNombre(), pulsoUmbrio);
            
            Movimiento afilaGarras = new MovimientoEstado("Afilagarras", tipoSiniestro, 0, 0, 15, 15, false, "estado", "Aumenta el ataque del usuario");
            movimientos.put(afilaGarras.getNombre(), afilaGarras);
            
            // Dragon types movements
            Movimiento danzaDragon = new MovimientoEstado("Danza Dragon", tipoDragon, 0, 0, 20, 20, false, "estado", "Aumenta la velocidad y ataque del usuario");
            movimientos.put(danzaDragon.getNombre(), danzaDragon);
            
            Movimiento garraDragon = new MovimientoFisico("Garra Dragon", tipoDragon, 80, 100, 15, 15, false, "físico", "daño al oponente");
            movimientos.put(garraDragon.getNombre(), garraDragon);
            
            Movimiento cometaDraco = new MovimientoEspecial("Cometa Draco", tipoDragon, 130, 90, 5, 5, false, "especial", "daño al oponente");
            movimientos.put(cometaDraco.getNombre(), cometaDraco);
            
            // Electric types movements
            Movimiento rayo = new MovimientoEspecial("Rayo", tipoElectrico, 90, 100, 15, 15, false, "especial", "10% de probabilidad de paralizar al oponente");
            movimientos.put(rayo.getNombre(), rayo);
            
            Movimiento colmilloTrueno = new MovimientoFisico("Colmillo Trueno", tipoElectrico, 65, 95, 15, 15, false, "físico", "10% de probabilidad de paralizar al oponente");
            movimientos.put(colmilloTrueno.getNombre(), colmilloTrueno);
            
            Movimiento ondaTrueno = new MovimientoEstado("Onda Trueno", tipoElectrico, 0, 90, 20, 20, false, "estado", "Paraliza al oponente");
            movimientos.put(ondaTrueno.getNombre(), ondaTrueno);
            
            // Fairy types movements
            Movimiento carantoña = new MovimientoFisico("Carantoña", tipoHada, 90, 100, 10, 10, false, "físico", "10% de probabilidad de reducir el ataque del oponente");
            movimientos.put(carantoña.getNombre(), carantoña);
            
            Movimiento fuerzaLunar = new MovimientoEspecial("Fuerza Lunar", tipoHada, 95, 100, 15, 15, false, "especial", "30% de probabilidad de reducir el ataque especial del oponente");
            movimientos.put(fuerzaLunar.getNombre(), fuerzaLunar);
            
            Movimiento encanto = new MovimientoEstado("Encanto", tipoHada, 0, 100, 20, 20,false, "estado", "Reduce el ataque del oponente");
            movimientos.put(encanto.getNombre(), encanto);
            
            // Fighting types movements
            Movimiento esferaAural = new MovimientoEspecial("Esfera Aural", tipoLucha, 80, 100, 20, 20,false, "especial", "daño al oponente");
            movimientos.put(esferaAural.getNombre(), esferaAural);
            
            Movimiento aBocajarro = new MovimientoFisico("A Bocajarro", tipoLucha, 120, 100, 5, 5,false, "físico", "daño al oponente");
            movimientos.put(aBocajarro.getNombre(), aBocajarro);
            
            Movimiento corpulencia = new MovimientoEstado("Corpulencia", tipoLucha, 0, 0, 20, 20,false, "estado", "Aumenta el ataque y defensa del usuario");
            movimientos.put(corpulencia.getNombre(), corpulencia);
            
            // Fire types movements 
            Movimiento lanzallamas = new MovimientoEspecial("Lanzallamas", tipoFuego, 90, 100, 15, 15,false, "especial", "10% de probabilidad de quemar al oponente");
            movimientos.put(lanzallamas.getNombre(), lanzallamas);
            
            Movimiento fuegoFatuo = new MovimientoEstado("Fuegofatuo", tipoFuego, 0, 85, 15, 15,false, "estado", "Quema al oponente");
            movimientos.put(fuegoFatuo.getNombre(), fuegoFatuo);
            
            Movimiento ruedaFuego = new MovimientoFisico("Rueda Fuego", tipoFuego, 60, 100, 25, 25,false, "físico", "10% de probabilidad de quemar al oponente");
            movimientos.put(ruedaFuego.getNombre(), ruedaFuego);
            
            // Flying types movements
            Movimiento respiro = new MovimientoEstado("Respiro", tipoVolador, 0, 0, 10, 10,false, "estado", "Recupera la mitad de los PS máximos del usuario");
            movimientos.put(respiro.getNombre(), respiro);
            
            Movimiento pajaroOsado = new MovimientoFisico("Pájaro Osado", tipoVolador, 120, 100, 15, 15,false, "físico", "daño al oponente");
            movimientos.put(pajaroOsado.getNombre(), pajaroOsado);
            
            Movimiento huracan = new MovimientoEspecial("Huracán", tipoVolador, 110, 70, 10, 10,false, "especial", "30% de probabilidad de confundir al oponente");
            movimientos.put(huracan.getNombre(), huracan);
            
            // Ghost types movements
            Movimiento bolaSombra = new MovimientoEspecial("Bola Sombra", tipoFantasma, 80, 100, 15, 15,false, "especial", "20% de probabilidad de reducir la defensa especial del oponente");
            movimientos.put(bolaSombra.getNombre(), bolaSombra);
            
            Movimiento garraUmbria = new MovimientoFisico("Garra Umbría", tipoFantasma, 70, 100, 15, 15,false, "físico", "daño al oponente");
            movimientos.put(garraUmbria.getNombre(), garraUmbria);
            
            Movimiento rayoConfuso = new MovimientoEstado("Rayo Confuso", tipoFantasma, 0, 100, 10, 10,false, "estado", "Confunde al oponente");
            movimientos.put(rayoConfuso.getNombre(), rayoConfuso);
            
            // Grass types movements
            Movimiento rayoSolar = new MovimientoEspecial("Rayo Solar", tipoPlanta, 120, 100, 10, 10,false, "especial", "daño al oponente");
            movimientos.put(rayoSolar.getNombre(), rayoSolar);
            
            Movimiento hojaAguda = new MovimientoFisico("Hoja Aguda", tipoPlanta, 90, 100, 15, 15,false, "físico", "daño al oponente");
            movimientos.put(hojaAguda.getNombre(), hojaAguda);
            
            Movimiento paralizador = new MovimientoEstado("Paralizador", tipoPlanta, 0, 75, 30, 30,false, "estado", "Paraliza al oponente");
            movimientos.put(paralizador.getNombre(), paralizador);
            
            // Ground types movements
            Movimiento terremoto = new MovimientoFisico("Terremoto", tipoTierra, 100, 100, 10, 10,false, "físico", "daño al oponente");
            movimientos.put(terremoto.getNombre(), terremoto);
            
            Movimiento disparoLodo = new MovimientoEspecial("Disparo Lodo", tipoTierra, 55, 95, 15, 15,false, "especial", "Reducir la velocidad del oponente");
            movimientos.put(disparoLodo.getNombre(), disparoLodo);
            
            Movimiento ataqueArena = new MovimientoEstado("Ataque Arena", tipoTierra, 0, 100, 15, 15, false, "estado", "Reduce la precisión del oponente");
            movimientos.put(ataqueArena.getNombre(), ataqueArena);
            
            // Ice types movements
            Movimiento puñoHielo = new MovimientoFisico("Puño Hielo", tipoHielo, 75, 100, 15, 15, false, "físico", "10% de probabilidad de congelar al oponente");
            movimientos.put(puñoHielo.getNombre(), puñoHielo);
            
            Movimiento rayoHielo = new MovimientoEspecial("Rayo Hielo", tipoHielo, 90, 100, 10, 10,false, "especial", "10% de probabilidad de congelar al oponente");
            movimientos.put(rayoHielo.getNombre(), rayoHielo);
            
            Movimiento veloAurora = new MovimientoEstado("Velo Aurora", tipoHielo, 0, 0, 20, 20,false, "estado", "Aumenta la defensa especial y la defensa del usuario");
            movimientos.put(veloAurora.getNombre(), veloAurora);
            
            // Normal types movements
            Movimiento golpeCuerpo = new MovimientoFisico("Golpe Cuerpo", tipoNormal, 85, 100, 15, 15,false, "físico", "30% de probabilidad de paralizar al oponente");
            movimientos.put(golpeCuerpo.getNombre(), golpeCuerpo);
            
            Movimiento hiperrayo = new MovimientoEspecial("Hiperrayo", tipoNormal, 150, 90, 5, 5,false, "especial", "daño al oponente");
            movimientos.put(hiperrayo.getNombre(), hiperrayo);
            
            Movimiento tambor = new MovimientoEstado("Tambor", tipoNormal, 0, 0, 10, 10,false, "estado", "Reduce los PS del usuario a la mitad y aumenta su ataque al máximo");
            movimientos.put(tambor.getNombre(), tambor);
            
            // Poison types movements
            Movimiento puyaNociva = new MovimientoFisico("Puya Nociva", tipoVeneno, 80, 100, 20, 20,false, "físico", "30% de probabilidad de envenenar al oponente");
            movimientos.put(puyaNociva.getNombre(), puyaNociva);
            
            Movimiento toxico = new MovimientoEstado("Tóxico", tipoVeneno, 0, 90, 10, 10,false, "estado", "Envenena al oponente");
            movimientos.put(toxico.getNombre(), toxico);
            
            Movimiento bombaLodo = new MovimientoEspecial("Bomba Lodo", tipoVeneno, 90, 100, 10, 10,false, "especial", "30% de probabilidad de envenenar al oponente");
            movimientos.put(bombaLodo.getNombre(), bombaLodo);
            
            // Psychic types movements
            Movimiento psiquico = new MovimientoEspecial("Psíquico", tipoPsiquico, 90, 100, 10, 10,false, "especial", "10% de probabilidad de reducir la defensa especial del oponente");
            movimientos.put(psiquico.getNombre(), psiquico);
            
            Movimiento cabezazoZen = new MovimientoFisico("Cabezazo Zen", tipoPsiquico, 80, 90, 15, 15,false, "físico", "daño al oponente");
            movimientos.put(cabezazoZen.getNombre(), cabezazoZen);
            
            Movimiento hipnosis = new MovimientoEstado("Hipnosis", tipoPsiquico, 0, 60, 20, 20,false, "estado", "Duerme al oponente");
            movimientos.put(hipnosis.getNombre(), hipnosis);
            
            // Rock types movements
            Movimiento rocaAfilada = new MovimientoFisico("Roca Afilada", tipoRoca, 100, 80, 5, 5,false, "físico", "daño al oponente");
            movimientos.put(rocaAfilada.getNombre(), rocaAfilada);
            
            Movimiento poderPasado = new MovimientoEspecial("Poder Pasado", tipoRoca, 60, 100, 5, 5,false, "especial", "10% de probabilidad de aumentar todas las características del usuario");
            movimientos.put(poderPasado.getNombre(), poderPasado);
            
            Movimiento pulimento = new MovimientoEstado("Pulimento", tipoRoca, 0, 0, 20, 20,false, "estado", "Aumenta la velocidad del usuario");
            movimientos.put(pulimento.getNombre(), pulimento);
            
            // Steel types movements
            Movimiento garraMetal = new MovimientoFisico("Garra Metal", tipoAcero, 50, 95, 35, 35,false, "físico", "10% de probabilidad de aumentar el ataque del usuario");
            movimientos.put(garraMetal.getNombre(), garraMetal);
            
            Movimiento focoResplandor = new MovimientoEspecial("Foco Resplandor", tipoAcero, 80, 100, 10, 10,false, "especial", "10% de probabilidad de reducir la defensa especial del oponente");
            movimientos.put(focoResplandor.getNombre(), focoResplandor);
            
            Movimiento ecoMetalico = new MovimientoEstado("Eco Metálico", tipoAcero, 0, 85, 40, 40,false, "estado", "Reduce la defensa especial del oponente");
            movimientos.put(ecoMetalico.getNombre(), ecoMetalico);
            
            // Water types movements
            Movimiento hidrobomba = new MovimientoEspecial("Hidrobomba", tipoAgua, 110, 80, 5, 5,false, "especial", "daño al oponente");
            movimientos.put(hidrobomba.getNombre(), hidrobomba);
            
            Movimiento buceo = new MovimientoFisico("Buceo", tipoAgua, 80, 100, 10, 10,false, "físico", "daño al oponente");
            movimientos.put(buceo.getNombre(), buceo);
            
            Movimiento refugio = new MovimientoEstado("Refugio", tipoAgua, 0, 0, 10, 10,false, "estado", "Aumenta la defensa del usuario");
            movimientos.put(refugio.getNombre(), refugio);
        
            try{
                ObjectOutputStream archivo = new ObjectOutputStream(new FileOutputStream("movimientos.bin"));
    
                archivo.writeObject(movimientos);
                archivo.close();
    
            }catch (IOException e) {
                System.out.println("Error al guardar los pokemones en el archivo: " + e.getMessage());
            

            }
    }
}

/**
 * conjunto de estados
 * estado inicial
 * estados finales
 * alfabeto
 * funcion de transicion
 */
package automata;

import com.inamik.utils.SimpleTableFormatter;
import com.inamik.utils.TableFormatter;
import excepcion.SimboloNoExistente;
import java.util.Iterator;
import java.util.Vector;
import excepcion.EstadoNoValidoException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Magda
 *
 */
public abstract class Automata {
	private int estadoInicial;
	private Alfabeto alfabeto;
        private Vector<Integer> estadosFinales;
        private Vector<Estado> estados;
	// vector de arreglos de vectores de estado (tabla de 3 dimensiones)
        //
	private Vector<Vector<Estado>[]> tabla;
	
	public Automata(Estado estadoInicial,Alfabeto alfabeto,Vector<Estado> estadosFinales,Vector<Estado> estados,Vector<Transicion> funcion) throws EstadoNoValidoException {
             try {

                 this.estadosFinales = new Vector<Integer>();
                    this.alfabeto = alfabeto;
                    this.estados = estados;

                    // creo tabla
                    crearTabla();
                    // seteo estado inicial
                    setEstadoInicial(estadoInicial);
                    // inicializo estados finales
                    setEstadosFinales(estadosFinales);

                    // inicilizo tabla (cargo transiciones)
                    Iterator<Transicion> itTransicion = funcion.iterator();

                    while(itTransicion.hasNext()){
                        Transicion transicion = itTransicion.next();
                        setTransicion(transicion);
                    }
            } catch (SimboloNoExistente ex) {
                    Logger.getLogger(Automata.class.getName()).log(Level.SEVERE, null, ex);
                    }
        }

	
	public void setAlfabeto(Alfabeto alfabeto){
            this.alfabeto = alfabeto;
	}
	
	public Alfabeto getAlfabeto(){
            return alfabeto;
        }

	public Vector<Estado> mover(Estado origen, Simbolo simbolo) throws SimboloNoExistente{
             return tabla.get(origen.getCodigo())[alfabeto.getIndice(simbolo)];

	}

        protected  int getIndice(Estado estado) throws EstadoNoValidoException{
            Estado estadoValido = checkEstado(estado);
            return estadoValido.getCodigo();

        }
         public void setTransicion(Transicion transicion) throws EstadoNoValidoException, SimboloNoExistente {

        // recupero estado origen
        // busco el estado origen (XO) en el vector de estados
        /////// A
        /////// si no lo encuentro lanzo exception
        /////// B
        /////// si lo encuentro
        /////// recupero el estado encontrado (YO)
        /////// descarto el estado origen XO
        /////// obtengo el codigo de YO (CYO)
        /////// busco el estado destino XD
        ////////// A
        ////////// si no lo encuentro lanzo la excepcion
        ////////// B
        ////////// si lo encuentro
        ////////// recupero el estado encontrado (YD)
        ////////// descarto el estado destino (YD)
        ////////// obtengo el codigo de YD (CYD)
        ////////// obtengo el indice del simbolo (IS)
        ////////// agrego el estado YD en la posicion CYO-IS de la tabla
        Estado estadoOrigen = null;
        Estado estadoDestino = null;
        Simbolo simbolo = null;

        estadoOrigen = checkEstado(transicion.getEstadoOrigen());
        estadoDestino = checkEstado(transicion.getEstadoDestino());
        simbolo = transicion.getSimbolo();

        mover(estadoOrigen, simbolo).add(estadoDestino);
        }

        private Estado checkEstado(Estado estado) throws EstadoNoValidoException {
        Iterator<Estado> itEstado = estados.iterator();
        Estado estadoValido = null;

        while (itEstado.hasNext()) {

            Estado estadoActual = itEstado.next();
            if (estadoActual.equals(estado)) {
                estadoValido = estadoActual;
                break;
            }
        }

        if (estadoValido == null) {
            throw new EstadoNoValidoException("Estado no valido");
        }
        return estadoValido;
    }

        public void setEstadoInicial(Estado estado) throws EstadoNoValidoException{
            estadoInicial = getIndice(estado);
        }

        public int getEstadoInicial(){
            return estadoInicial;
        }

        public void setEstadosFinales(Vector<Estado> estados) throws EstadoNoValidoException{
            Iterator<Estado> itEstados = estados.iterator();
            Vector<Integer> aux = new Vector<Integer>();

            while (itEstados.hasNext()){
                aux.add(new Integer(getIndice(itEstados.next())));
            }

            estadosFinales.addAll(aux);
        }

        public Vector<Integer> getEstadosFinales(){
            return estadosFinales;
        }

        public void setEstados(Vector<Estado> estados){
            this.estados = estados;
        }

        public void crearTabla(){
            // creo el vector de estados origen (Y)
            // recorro el vector de estados (x)
            // por cada estado
            ////// obtengo la cantidad de simbolos que tiene el alfabeto
            ////// creo el arreglo de longitud = a la cantidad de simbolos
            ////// por cada posicion del arreglo
            /////////// creo un nuevo vector de destinos (Z)
            ////// agrego el arreglo al vector estados origen (Y)
            ////// al estado actual del vector X le asigo el codigo = a la posicion actual del vector Y

            Iterator<Estado> itEstados = estados.iterator();
            tabla = new Vector<Vector<Estado>[]>();
            int cantSimbolos = alfabeto.getCantidadSimbolos();

            while (itEstados.hasNext()){
                Vector simbolos[] = new Vector[cantSimbolos];
                for (int i = 0;i < simbolos.length; i++){
                    simbolos[i] = new Vector<Estado>();
                }
                tabla.add(simbolos);
                itEstados.next().setCodigo(tabla.indexOf(simbolos));
            }
        }

        public Vector<Estado> getEstados(){
            return estados;
        }

        private Estado getEstado(int codigo){
            // recorro el vector de estados
            //// busco el estado que tiene el codigo correspondiente
            //// retorno el estado

            Iterator<Estado> itEstado = estados.iterator() ;

            while (itEstado.hasNext()){
                Estado estadoActual = itEstado.next();
                if (estadoActual.getCodigo() == codigo){
                    return estadoActual;
                }
            }
            return null;
        }

      /**
	 * retorna la informacion deseada
	 */
        @Override
	public String toString(){
//            //return  getEstadoOrigen() + "--" + getSimbolo() + "------>" + getEstadoOrigen();
//
//            StringBuffer salida = new StringBuffer();
//            int indEstOrigen,indSimbolos,indEstDestino;
//
//            int lenEstOrigen = tabla[0][0].length;
//            int lenSimbolos = tabla[0].length;
//            int lenEstDestino = tabla.length;
//
//            // Recorrido de las filas de la matriz
//            for (indEstOrigen=0; indEstOrigen<lenEstOrigen; indEstOrigen++){
//            // Recorrido de las celdas de una fila
//                for (indSimbolos=0; indSimbolos<lenSimbolos; indSimbolos++){
//                    for (indEstDestino=0; indEstDestino<lenEstDestino; indEstDestino++){
//                        salida.append("Estado Origen: ");
//                        salida.append(indEstOrigen);
//                        salida.append("Simbolo: ");
//                        salida.append(tabla.get(getIndice(estados)));
//                        salida.append("Estado Destino: ");
//                        salida.append(tabla[indEstadoOrigen][indSimbolo][indEstadoDestino]);
//                    }
//                 }
//            }
//            return salida.toString();

            TableFormatter tf = new SimpleTableFormatter(true);
            TableFormatter encabezado = tf.nextRow();
            encabezado = encabezado.nextCell().addLine("···");

            for (int i = 0;i < alfabeto.getCantidadSimbolos();i++){
                encabezado = encabezado.nextCell().addLine(alfabeto.getSimbolo(i).getNombre());
            }

            TableFormatter fila = encabezado;
            TableFormatter celda;
            TableFormatter linea = new SimpleTableFormatter();
            for (int i = 0; i < tabla.size(); ++i){
                fila = fila.nextRow();
                celda = fila.nextCell();
                celda = celda.addLine(getEstado(i).getNombre());
                for (int j = 0;j < tabla.get(i).length;j++){
                    celda = celda.nextCell();

                    for (int k = 0;k < tabla.get(i)[j].size();k++){
                        linea = celda.addLine(tabla.get(i)[j].get(k).getNombre());
                        celda = linea;
                    }
                }
                fila = celda;
            }

            String[] table = tf.getFormattedTable();
            StringBuffer salida = new StringBuffer();

		for (int i = 0, size = table.length; i < size; i++)
		{
			salida.append( (i + 1) + "\t" + table[i]);
		}

            return salida.toString();

        }
}

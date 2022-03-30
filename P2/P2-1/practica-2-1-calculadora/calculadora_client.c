/*
Programa hecho por Manuel Contreras Orge
*/


#include "calculadora.h"
#include <stdio.h>
#include <math.h>


void calculadora_1(char *server, double n1, double n2, char op) {
	CLIENT *clnt;
	double *resultado_operacion;

#ifndef	DEBUG
	clnt = clnt_create (server, CALCULADORA, CALCULADORA_BASICA, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (server);
		exit (1);
	}
#endif	/* DEBUG */

	/* Operación resultante según el operador seleccionado en la llamada */
	if (op == '+') {
		resultado_operacion = sumar_1(n1, n2, clnt);
	}
	else if (op == '-') {
		resultado_operacion = restar_1(n1, n2, clnt);
	}
	else if (op == '*' || op == 'x') {
		resultado_operacion = multiplicar_1(n1, n2, clnt);
	}
	else if (op == '/') {
		resultado_operacion = dividir_1(n1, n2, clnt);
	}
	else if (op == '%') {
		resultado_operacion = modulo_1(n1, n2, clnt);
	}
	else if (op == '^') {
		resultado_operacion = elevar_1(n1, n2, clnt);
	}
	else if (op == 'r') {
		resultado_operacion = raiz_cuadrada_1(n1, clnt);
	}
	else if (op == 'l') {
		resultado_operacion = logaritmo_1(n1, clnt);
	}
	else if (op == 'a') {
		resultado_operacion = valor_absoluto_1(n1, clnt);
	}
	else {
		fprintf(stderr, "ERROR, se ha introducido operador no existente (elegir entre +, -, *, /, %, ^, r, a, l)\n");
		exit(1);
	}

	if (resultado_operacion == (double *) NULL) {
		clnt_perror (clnt, "call failed");
		exit(1);
	}

	/* Imprime el resultado */
	printf("El resultado de la operacion solicitada es: %f\n", *resultado_operacion);

	#ifndef	DEBUG
		clnt_destroy (clnt);
	#endif	 /* DEBUG */
}

/****************************************************************************/

int main (int argc, char *argv[]) {
	char *server;
	double n1, n2;
	char op;
	int si = 0;

	if (argc != 5 && argc != 4) {
		printf ("ERROR, uso: %s <servidor> <primer numero> <operador> <segundo numero (opcional)>\n", argv[0]);
		exit (1);
	}
	else {
		if (argc == 4 && (*argv[3] != 'r' && *argv[3] != 'l' && *argv[3] != 'a')) {
			printf ("ERROR, uso: %s <servidor> <primer numero> <operador(r, l, a)>\n", argv[0]);
			exit (1);
		}
		else
			si = 1;	
	}
	
	server = argv[1];
	n1 = atoi(argv[2]);
	op = *argv[3];
	if (si == 0) 
		n2 = atoi(argv[4]); 
	else 
		n2 = 0;
	

	calculadora_1 (server, n1, n2, op);
	exit (0);
}

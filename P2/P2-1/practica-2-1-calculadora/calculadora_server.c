/*
Programa hecho por Manuel Contreras Orge
*/


#include "calculadora.h"
#include <stdio.h>
#include <math.h>

double * sumar_1_svc(double n1, double n2,  struct svc_req *rqstp) {
	static double resultado_sumar;

	resultado_sumar = (n1 + n2);

	return &resultado_sumar;
}

double * restar_1_svc(double n1, double n2,  struct svc_req *rqstp) {
	static double resultado_restar;

	resultado_restar = (n1 - n2);

	return &resultado_restar;
}

double * multiplicar_1_svc(double n1, double n2,  struct svc_req *rqstp) {
	static double resultado_multiplicar;

	resultado_multiplicar = (n1 * n2);

	return &resultado_multiplicar;
}

double * dividir_1_svc(double n1, double n2,  struct svc_req *rqstp) {
	static double resultado_dividir;

	resultado_dividir = (n1 / n2);

	return &resultado_dividir;
}

double * modulo_1_svc(int n1, int n2,  struct svc_req *rqstp) {
	static double resultado_modulo;

	resultado_modulo = (n1 % n2);

	return &resultado_modulo;
}

double * elevar_1_svc(double n1, double n2,  struct svc_req *rqstp) {
	static double resultado_elevar;

	resultado_elevar = pow(n1, n2);

	return &resultado_elevar;
}

double * raiz_cuadrada_1_svc(int n1,  struct svc_req *rqstp) {
	static double resultado_raiz_cuadrada;

	resultado_raiz_cuadrada = sqrt(n1);

	return &resultado_raiz_cuadrada;
}

double * logaritmo_1_svc(int n1,  struct svc_req *rqstp) {
	static double resultado_logaritmo;

	resultado_logaritmo = log(n1);

	return &resultado_logaritmo;
}

double * valor_absoluto_1_svc(int n1,  struct svc_req *rqstp) {
	static double resultado_valor_absoluto;

	resultado_valor_absoluto = fabs(n1);

	return &resultado_valor_absoluto;
}

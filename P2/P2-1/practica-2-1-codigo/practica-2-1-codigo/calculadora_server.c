/*
Programa hecho por Manuel Contreras Orge
*/


#include "calculadora.h"
#include <stdio.h>
#include <math.h>

double * sumar_1_svc(double n1, double n2,  struct svc_req *rqstp) {
	static double result;

	result = (n1 + n2);

	return &result;
}

double * restar_1_svc(double n1, double n2,  struct svc_req *rqstp) {
	static double result;

	result = (n1 - n2);

	return &result;
}

double * multiplicar_1_svc(double n1, double n2,  struct svc_req *rqstp) {
	static double result;

	result = (n1 * n2);

	return &result;
}

double * dividir_1_svc(double n1, double n2,  struct svc_req *rqstp) {
	static double result;

	result = (n1 / n2);

	return &result;
}

double * modulo_1_svc(int n1, int n2,  struct svc_req *rqstp) {
	static double result;

	result = (n1 % n2);

	return &result;
}

double * elevar_1_svc(double n1, double n2,  struct svc_req *rqstp) {
	static double result;

	result = (double)(pow((double)n1, (double)n2));

	return &result;
}

double * raiz_cuadrada_1_svc(int n1,  struct svc_req *rqstp) {
	static double result;

	result = sqrt(n1);

	return &result;
}

double * logaritmo_1_svc(int n1,  struct svc_req *rqstp) {
	static double result;

	result = log(n1);

	return &result;
}

double * valor_absoluto_1_svc(int n1,  struct svc_req *rqstp) {
	static double result;

	result = fabs(n1);

	return &result;
}

from calculadora import Calculadora

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

import sys, os

transport = TSocket.TSocket("localhost", 9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

client = Calculadora.Client(protocol)

transport.open()

print("Introduzca el primer número")
n1 = int(input())

print("Introduzca el operador")
op = input()

if not (op == 'l' or op == 'r' or op == 'a'):
	print("Introduzca el segundo número")
	n2 = int(input())
	print("\n")

print("hacemos ping al server\n")
client.ping()
if (op == '+'):
	resultado = client.suma(n1, n2)
	print(n1, "+", n2, "=" , str(resultado))
elif (op == '-'):
	resultado = client.resta(n1, n2)
	print(n1, "-", n2, "=" , str(resultado))
elif (op == '*' or op == 'x'):
	resultado = client.multiplicacion(n1, n2)
	print(n1, "*", n2, "=" , str(resultado))
elif (op == '/'):
	resultado = client.division(n1, n2)
	print(n1, "/", n2, "=" , str(resultado))
elif (op == '%'):
	resultado = client.modulo(n1, n2)
	print(n1, "%", n2, "=" , str(resultado))
elif (op == '^'):
	resultado = client.elevar(n1, n2)
	print(n1, "^", n2, "=" , str(resultado))
elif (op == 'r'):
	resultado = client.raiz_cuadrada(n1)
	print("√" + str(n1) + " = " + str(resultado))
elif (op == 'l'):
	resultado = client.logaritmo(n1)
	print("log(" + str(n1) + ") = " + str(resultado))
elif (op == 'a'):
	resultado = client.valor_absoluto(n1)
	print("|" + str(n1) + "| = " + str(resultado))
else:
	print("ERROR, se ha introducido operador no existente (elegir entre +, -, *-x, /, %, ^, r, a, l)\n")
	sys.exit(0)	
	
transport.close()

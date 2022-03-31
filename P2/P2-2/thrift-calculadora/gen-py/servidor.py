import glob
import sys

from calculadora import Calculadora

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

import logging
import math

logging.basicConfig(level=logging.DEBUG)


class CalculadoraHandler:
    def __init__(self):
        self.log = {}

    def ping(self):
        print("me han hecho ping()")

    def suma(self, n1, n2):
        print("sumando " + str(n1) + " con " + str(n2))
        return n1 + n2

    def resta(self, n1, n2):
        print("restando " + str(n1) + " con " + str(n2))
        return n1 - n2

    def multiplicacion(self, n1, n2):
        print("multiplicando " + str(n1) + " con " + str(n2))
        return n1 * n2

    def division(self, n1, n2):
        print("dividiendo " + str(n1) + " entre " + str(n2))
        return n1 / n2
        
    def modulo(self, n1, n2):
        print("modulo de " + str(n1) + " entre " + str(n2))
        return n1 %  n2
        
    def elevar(self, n1, n2):
        print("elevando " + str(n1) + " a " + str(n2))
        return pow(n1, n2)
        
    def raiz_cuadrada(self, n1):
        print("raiz cuadrada de " + str(n1))
        return math.sqrt(n1)
        
    def logaritmo(self, n1):
        print("logaritmo de " + str(n1))
        return math.log(n1)
        
    def valor_absoluto(self, n1):
        print("valor absoluto de " + str(n1))
        return abs(n1)
        
        #faltan las operaciones logaritmo y valor absoluto
        


if __name__ == "__main__":
    handler = CalculadoraHandler()
    processor = Calculadora.Processor(handler)
    transport = TSocket.TServerSocket(host="127.0.0.1", port=9090)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

    print("iniciando servidor...")
    server.serve()
    print("fin")

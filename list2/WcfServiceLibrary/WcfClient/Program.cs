
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Resources;
using System.Security.Cryptography;
using System.ServiceModel;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using WcfClient.ServiceReference1;
using WcfClient.ServiceReference2;
using WcfClient.ServiceReference3;
using WcfServiceLibraryMod;

namespace WcfClient
{
    internal class Program
    {
        private static CalculatorClient myClient2;
        private static ListClient myClient4;

        static void Main(string[] args)
        {
            Console.WriteLine("... The client is started");
            // Step 1: Create client proxy based on communication channel.
            // base address:
            Uri baseAddress;
            Uri baseAddress2;
            // binding, address, endpoint address:
            BasicHttpBinding myBinding = new BasicHttpBinding();
            baseAddress = new Uri("http://localhost:10001/MakaraService/endpoint1");

            BasicHttpBinding myBinding2 = new BasicHttpBinding();
            baseAddress2 = new Uri("http://localhost:10001/ListService/endpoint4");


            EndpointAddress eAddress = new EndpointAddress(baseAddress);
            EndpointAddress eAddress2 = new EndpointAddress(baseAddress2);
            // channel factory:
            ChannelFactory<ICalculator> myCF = new ChannelFactory<ICalculator>(myBinding, eAddress);
    
            ICalculator myClient = myCF.CreateChannel();

 
            // client proxy (here myClient) based on channel
            myClient2 = new CalculatorClient("WSHttpBinding_ICalculator");

            myClient4 = new ListClient("WSHttpBinding_IList");



            // Step 2: service operations call.
            Console.Write("...calling Add (for entpoint1) ");
            double result = myClient.Add(-3.7, 9.5); //just example values
            Console.WriteLine("Result = " + result);


            Console.Write("...calling Multiply (for endpoint2) - ");
            result = myClient2.Multiply(-3.7, 9.5); //just example values
            Console.WriteLine("Result = " + result);

            Console.WriteLine("2...calling HMultiply ASYNCHRONOUSLY !!!");
            Task<double> asyResult = callHMultiplyAsy(1.1, -3.3);
            Thread.Sleep(100);




            Console.WriteLine("3...calling GetDATA ASYNCHRONOUSLY !!!");
            Task<Record[]> asyResult2 = callListAsync();
            Thread.Sleep(100);




            SuperCalcCallback myCbHandler = new SuperCalcCallback();
            InstanceContext instanceContext = new InstanceContext(myCbHandler);
            SuperCalcClient myClient3 = new SuperCalcClient(instanceContext);
            double value1 = 10;
            Console.WriteLine("...calling Factorial({0})...", value1);
            myClient3.Factorial(value1);



            Console.Write("...calling Add (for entpoint1) ");
            double result3 = myClient.Add(-3.7, 9.5); //just example values
            Console.WriteLine("Result = " + result3);



            Console.WriteLine("...press <ENTER> to STOP client...");
            Console.WriteLine();
            Console.ReadLine(); // to not finish app immediately:
                                // Step 3: Closing the client - closes connection and clears resources.
           
            ((IClientChannel)myClient).Close();
            result = asyResult.Result;
            Console.WriteLine("2...HMultiplyAsync Result = " + result);

            Record[] result2;
            result2 = (Record[])asyResult2.Result;
            Console.WriteLine("2...ListAsync Result = " + result2[0].StringValue);

            Console.WriteLine("...Client closed - FINISHED");
            myClient3.Close();
            Console.WriteLine("CLIENT3 - STOP");

 
        }

        static async Task<Record[]> callListAsync()
        {
            Console.WriteLine("2......called ListAsync");
            Record[] reply = await myClient4.GetDataAsync();
            Console.WriteLine("2......finished ListAsync");
            return reply;
        }

        static async Task<double> callHMultiplyAsy(double n1, double n2)
        {
            Console.WriteLine("2......called callHMultiplyAsync");
            double reply = await myClient2.HMultiplyAsync(n1, n2);
            Console.WriteLine("2......finished HMultipleAsync");
            return reply;
        }

    }
    internal class SuperCalcCallback : ISuperCalcCallback
    {
        public void FactorialResult(double result)
        {
            //here the result is consumed
            Console.WriteLine(" Factorial = {0}", result);
        }
    }
}

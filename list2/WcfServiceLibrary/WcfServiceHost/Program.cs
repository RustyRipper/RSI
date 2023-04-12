using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel.Description;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;
using WcfServiceLibrary;
using CallbackService;
using WcfServiceLibraryMod;

namespace WcfServiceHost
{
    internal class Program
    {
        static void Main(string[] args)
        {
            // Krok 1 URI dla bazowego adresu serwisu
            Uri baseAddress = new Uri("http://localhost:10001/MakaraService");
            Uri baseAddress3 = new Uri("http://localhost:10001/CallbackService");
            Uri baseAddress4 = new Uri("http://localhost:10001/ListService");
            // Krok 2 Instancja serwisu
            ServiceHost myHost = new ServiceHost(typeof(MyCalculator), baseAddress);
            ServiceHost myHost3 = new ServiceHost(typeof(MySuperCalc), baseAddress3);
            ServiceHost myHost4 = new ServiceHost(typeof(ListMod), baseAddress4);
            // Krok 3 Endpoint serwisu
            BasicHttpBinding myBinding = new BasicHttpBinding();
            ServiceEndpoint endpoint1 = myHost.AddServiceEndpoint(typeof(ICalculator), myBinding, "endpoint1");

            WSHttpBinding myBinding2 = new WSHttpBinding();
            myBinding2.Security.Mode = SecurityMode.None;
            ServiceEndpoint endpoint2 = myHost.AddServiceEndpoint(typeof(ICalculator), myBinding2, "endpoint2");

            WSDualHttpBinding myBinding3 = new WSDualHttpBinding();
            ServiceEndpoint endpoint3 = myHost3.AddServiceEndpoint(typeof(ISuperCalc), myBinding3, "endpoint3");

            WSHttpBinding myBinding4 = new WSHttpBinding();
            myBinding4.Security.Mode = SecurityMode.None;
            ServiceEndpoint endpoint4 = myHost4.AddServiceEndpoint(typeof(IList), myBinding4, "endpoint4");
            // Krok 4 Ustawienie metadanych
            ServiceMetadataBehavior smb = new ServiceMetadataBehavior();
            smb.HttpGetEnabled = true;
            myHost.Description.Behaviors.Add(smb);
            myHost3.Description.Behaviors.Add(smb);
            myHost4.Description.Behaviors.Add(smb);


            try
            {
                // Krok 5 Uruchomienie serwisu.
                myHost.Open();
                Console.WriteLine("\n---> Endpoints:");
                Console.WriteLine("\nService endpoint {0}:", endpoint1.Name);
                Console.WriteLine("Binding: {0}", endpoint1.Binding.ToString());
                Console.WriteLine("ListenUri: {0}", endpoint1.ListenUri.ToString());
                Console.WriteLine("\n---> Endpoints2:");
                Console.WriteLine("\nService endpoint2 {0}:", endpoint2.Name);
                Console.WriteLine("Binding2: {0}", endpoint2.Binding.ToString());
                Console.WriteLine("ListenUri2: {0}", endpoint2.ListenUri.ToString());

                myHost3.Open();
                Console.WriteLine("--> Service SuperCalc is running.");

                myHost4.Open();
                Console.WriteLine("--> Service List is running.");

                Console.WriteLine("Service is started and running.");
                Console.WriteLine("Press <ENTER> to STOP service...");
                Console.WriteLine();
                Console.ReadLine(); // aby nie kończyć natychmiast:
                myHost.Close();
                myHost3.Close();
                myHost4.Close();
            }
            catch (CommunicationException ce)
            {
                Console.WriteLine("Exception occured: {0}", ce.Message);
                myHost.Abort();
                myHost3.Abort();
                myHost4.Abort();
            }
        }

    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.NetworkInformation;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.Threading;

namespace WcfServiceLibrary
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in both code and config file together.
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single,
        ConcurrencyMode = ConcurrencyMode.Multiple)]
    public class MyCalculator : ICalculator
    {
        public double Add(double val1, double val2)
        {
            Console.WriteLine("..Called ADD");
            return val1 + val2;
        }

        public double Multiply(double val1, double val2)
        {
            Console.WriteLine("..Called Multiply");
            return (val1 * val2);
        }

        public double HMultiply(double val1, double val2)
        {
            Thread.Sleep(3000);
            Console.WriteLine("..Called HMultiply");
            return (val1 * val2);
        }
    }
}

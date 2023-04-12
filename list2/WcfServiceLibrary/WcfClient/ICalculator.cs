using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Security;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;

namespace WcfClient
{
    [ServiceContract(ProtectionLevel = ProtectionLevel.None)]
    public interface ICalculator
    {
        [OperationContract]
        double Add(double val1, double val2);
        [OperationContract]
        double Multiply(double val1, double val2);
        [OperationContract]
        double HMultiply(double val1, double val2);
    }
}

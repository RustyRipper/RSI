using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Net.Security;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;
using WcfServiceLibraryMod;

namespace WcfClient
{
    [ServiceContract(ProtectionLevel = ProtectionLevel.None)]
    public interface IList
    {
        [OperationContract]
        List<Record> GetData();

        [OperationContract]
        Record Add(Record composite);

    }
}

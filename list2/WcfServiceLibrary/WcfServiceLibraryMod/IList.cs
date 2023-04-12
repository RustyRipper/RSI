using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Net.Security;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WcfServiceLibraryMod
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService1" in both code and config file together.
    [ServiceContract(ProtectionLevel = ProtectionLevel.None)]
    public interface IList
    {
        [OperationContract]
        List<Record> GetData();

        [OperationContract]
        Record Add(Record composite);

    }

    [DataContract]
    public class Record
    {
        string stringValue = "Hello ";


        [DataMember]
        public string StringValue
        {
            get { return stringValue; }
            set { stringValue = value; }
        }
        public Record( string stringValue)
        {
            StringValue = stringValue;

        }
    }
}

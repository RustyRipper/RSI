using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WcfServiceLibraryMod
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in both code and config file together.
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single,
        ConcurrencyMode = ConcurrencyMode.Multiple)]
    public class ListMod : IList
    {
        List<Record> array = new List<Record>() { new Record("123")};
        public ListMod()
        {
            //array.Add(new Record("123"));
            //array.Add(new CompositeType());
            //array.Add(new CompositeType());
        }

        public List<Record> GetData()
        {
            return array;
        }

        public Record Add(Record composite)
        {
            array.Add(composite);
            if (composite == null)
            {
                throw new ArgumentNullException("composite");
            }
            return composite;
        }
    }
}

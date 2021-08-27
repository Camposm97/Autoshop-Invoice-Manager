using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Autoshop_Invoice_Manager
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void btExit_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void btCustomers_Click(object sender, EventArgs e)
        {
            Button btAdd = new Button
            {
                Name = "btAddCustomer",
                Location = new Point(11, 11),
                Size = new Size(220, 40),
                Text = "Hello World!",
                UseVisualStyleBackColor = true
            };

            this.splitContainer2.Panel2.Controls.Add(btAdd);
        }
    }
}

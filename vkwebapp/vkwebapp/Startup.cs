using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(vkwebapp.Startup))]
namespace vkwebapp
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}

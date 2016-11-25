using Microsoft.Owin;
using Owin;

[assembly: OwinStartup(typeof(VKAPI.Startup))]
namespace VKAPI
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
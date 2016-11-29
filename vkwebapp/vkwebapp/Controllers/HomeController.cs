using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Web;
using System.Data.Entity;
using System.Web.Mvc;
using vkwebapp.Models;
using System.Linq;

namespace vkwebapp.Controllers
{
    //
    /// <summary>
    /// Source of Image from http://soft1info.blogspot.ie/p/3d.html
    /// </summary>
    public class HomeController : Controller
    {
        private ServiceModels db = new ServiceModels();

        public async Task<ActionResult> Index()
        {
            int limit = 6;
            return View(await db.AuthApps.Take(limit).ToListAsync());
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your application description page.";

            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }
    }
}
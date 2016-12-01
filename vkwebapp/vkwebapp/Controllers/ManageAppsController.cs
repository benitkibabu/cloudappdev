using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Net;
using System.Web;
using System.Web.Mvc;
using vkwebapp.Models;
using Microsoft.AspNet.Identity;
using System.Security.Claims;

namespace vkwebapp.Controllers
{
    [Authorize]
    public class ManageAppsController : Controller
    {
        private ServiceModels db = new ServiceModels();

        // GET: ManageApps
        public async Task<ActionResult> Index()
        {
            string userId = User.Identity.GetUserId().ToString();
            return View(await db.AuthApps.Where(a=>a.UserID.Equals(userId)).ToListAsync());
        }

        // GET: ManageApps/Details/5
        public async Task<ActionResult> Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            AuthApp authApp = await db.AuthApps.FindAsync(id);
            if (authApp == null)
            {
                return HttpNotFound();
            }
            return View(authApp);
        }

        // GET: ManageApps/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: ManageApps/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "id,auth_key,app_name,UserID")] AuthApp authApp)
        {
            authApp.auth_key = Guid.NewGuid().ToString();
            authApp.UserID = User.Identity.GetUserId().ToString();
            if (authApp.app_name.Length > 1)
            {
                db.AuthApps.Add(authApp);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

            return View(authApp);
        }

        // GET: ManageApps/Edit/5
        public async Task<ActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            AuthApp authApp = await db.AuthApps.FindAsync(id);
            if (authApp == null)
            {
                return HttpNotFound();
            }
            return View(authApp);
        }

        // POST: ManageApps/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "id,auth_key,app_name,UserID")] AuthApp authApp)
        {
            if (ModelState.IsValid)
            {
                db.Entry(authApp).State = EntityState.Modified;
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            return View(authApp);
        }

        // GET: ManageApps/Delete/5
        public async Task<ActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            AuthApp authApp = await db.AuthApps.FindAsync(id);
            if (authApp == null)
            {
                return HttpNotFound();
            }
            return View(authApp);
        }

        // POST: ManageApps/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(int id)
        {
            AuthApp authApp = await db.AuthApps.FindAsync(id);
            db.AuthApps.Remove(authApp);
            await db.SaveChangesAsync();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}

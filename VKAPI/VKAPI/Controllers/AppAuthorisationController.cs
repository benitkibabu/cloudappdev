using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Net;
using System.Web;
using System.Web.Mvc;
using VKAPI.Models;

namespace VKAPI.Controllers
{
    public class AppAuthorisationController : Controller
    {
        private VkitchenContext db = new VkitchenContext();

        // GET: AppAuthorisation
        public async Task<ActionResult> Index()
        {
            return View(await db.auth_app.ToListAsync());
        }

        // GET: AppAuthorisation/Details/5
        public async Task<ActionResult> Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            auth_app auth_app = await db.auth_app.FindAsync(id);
            if (auth_app == null)
            {
                return HttpNotFound();
            }
            return View(auth_app);
        }

        // GET: AppAuthorisation/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: AppAuthorisation/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "id,app_name")] auth_app auth_app)
        {
            auth_app.auth_key = Guid.NewGuid().ToString();
            if (auth_app.app_name.Length >0 )
            {
                
                db.auth_app.Add(auth_app);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

            return View(auth_app);
        }

        // GET: AppAuthorisation/Edit/5
        public async Task<ActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            auth_app auth_app = await db.auth_app.FindAsync(id);
            if (auth_app == null)
            {
                return HttpNotFound();
            }
            return View(auth_app);
        }

        // POST: AppAuthorisation/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "id,auth_key,app_name")] auth_app auth_app)
        {
            if (ModelState.IsValid)
            {
                db.Entry(auth_app).State = EntityState.Modified;
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            return View(auth_app);
        }

        // GET: AppAuthorisation/Delete/5
        public async Task<ActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            auth_app auth_app = await db.auth_app.FindAsync(id);
            if (auth_app == null)
            {
                return HttpNotFound();
            }
            return View(auth_app);
        }

        // POST: AppAuthorisation/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(int id)
        {
            auth_app auth_app = await db.auth_app.FindAsync(id);
            db.auth_app.Remove(auth_app);
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

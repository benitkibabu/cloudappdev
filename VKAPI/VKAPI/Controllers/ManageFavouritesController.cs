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
    public class ManageFavouritesController : Controller
    {
        private VkitchenContext db = new VkitchenContext();

        // GET: ManageFavourites
        public async Task<ActionResult> Index()
        {
            var my_recipes = db.my_recipes.Include(m => m.user);
            return View(await my_recipes.ToListAsync());
        }

        // GET: ManageFavourites/Details/5
        public async Task<ActionResult> Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            my_recipes my_recipes = await db.my_recipes.FindAsync(id);
            if (my_recipes == null)
            {
                return HttpNotFound();
            }
            return View(my_recipes);
        }

        // GET: ManageFavourites/Create
        public ActionResult Create()
        {
            ViewBag.userid = new SelectList(db.users, "id", "logintype");
            return View();
        }

        // POST: ManageFavourites/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "id,uri,label,imageurl,source,url,shareas,yield,calories,totalweight,userid")] my_recipes my_recipes)
        {
            if (ModelState.IsValid)
            {
                db.my_recipes.Add(my_recipes);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

            ViewBag.userid = new SelectList(db.users, "id", "logintype", my_recipes.userid);
            return View(my_recipes);
        }

        // GET: ManageFavourites/Edit/5
        public async Task<ActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            my_recipes my_recipes = await db.my_recipes.FindAsync(id);
            if (my_recipes == null)
            {
                return HttpNotFound();
            }
            ViewBag.userid = new SelectList(db.users, "id", "logintype", my_recipes.userid);
            return View(my_recipes);
        }

        // POST: ManageFavourites/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "id,uri,label,imageurl,source,url,shareas,yield,calories,totalweight,userid")] my_recipes my_recipes)
        {
            if (ModelState.IsValid)
            {
                db.Entry(my_recipes).State = EntityState.Modified;
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            ViewBag.userid = new SelectList(db.users, "id", "logintype", my_recipes.userid);
            return View(my_recipes);
        }

        // GET: ManageFavourites/Delete/5
        public async Task<ActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            my_recipes my_recipes = await db.my_recipes.FindAsync(id);
            if (my_recipes == null)
            {
                return HttpNotFound();
            }
            return View(my_recipes);
        }

        // POST: ManageFavourites/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(int id)
        {
            my_recipes my_recipes = await db.my_recipes.FindAsync(id);
            db.my_recipes.Remove(my_recipes);
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

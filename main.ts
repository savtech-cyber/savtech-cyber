import { Hono } from "npm:hono";
import { cors } from "npm:hono/cors";
import { logger } from "npm:hono/logger";
import { createClient } from "npm:@supabase/supabase-js";
import * as kv from "./kv_store.ts";

const app = new Hono();

app.use(
  "*",
  cors({
    origin: "*",
    allowHeaders: ["*"],
    allowMethods: ["*"],
  }),
);
app.use("*", logger(console.log));

// Add validation for environment variables
const supabaseUrl = Deno.env.get("SUPABASE_URL");
const supabaseKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");

if (!supabaseUrl || !supabaseKey) {
  throw new Error("Missing SUPABASE_URL or SUPABASE_SERVICE_ROLE_KEY environment variables");
}

const supabase = createClient(supabaseUrl, supabaseKey);

// Business Management Routes
app.post(
  "/make-server-d8f234cd/businesses/register",
  async (c) => {
    try {
      const {
        name,
        description,
        city,
        contactEmail,
        contactPhone,
        category,
      } = await c.req.json();

      const businessId = crypto.randomUUID();
      const business = {
        id: businessId,
        name,
        description,
        city,
        contactEmail,
        contactPhone,
        category,
        verified: false,
        createdAt: new Date().toISOString(),
      };

      await kv.set(`business:${businessId}`, business);

      return c.json({ success: true, business });
    } catch (error) {
      console.log(`Error registering business: ${error}`);
      return c.json(
        { error: "Failed to register business" },
        500,
      );
    }
  },
);

app.get("/make-server-d8f234cd/businesses", async (c) => {
  try {
    const city = c.req.query("city");
    const category = c.req.query("category");

    const businesses = await kv.getByPrefix("business:");
    let filteredBusinesses = businesses;

    if (city && city !== "all") {
      filteredBusinesses = filteredBusinesses.filter(
        (b) => b.city.toLowerCase() === city.toLowerCase(),
      );
    }

    if (category && category !== "all") {
      filteredBusinesses = filteredBusinesses.filter(
        (b) =>
          b.category.toLowerCase() === category.toLowerCase(),
      );
    }

    return c.json({ businesses: filteredBusinesses });
  } catch (error) {
    console.log(`Error fetching businesses: ${error}`);
    return c.json({ error: "Failed to fetch businesses" }, 500);
  }
});

// Product Management Routes
app.post("/make-server-d8f234cd/products", async (c) => {
  try {
    const {
      businessId,
      name,
      description,
      price,
      category,
      imageUrl,
      stock,
      sku,
    } = await c.req.json();

    // Verify business exists
    const business = await kv.get(`business:${businessId}`);
    if (!business) {
      return c.json({ error: "Business not found" }, 404);
    }

    const productId = crypto.randomUUID();
    const product = {
      id: productId,
      businessId,
      name,
      description,
      price: parseFloat(price),
      category,
      imageUrl,
      stock: parseInt(stock),
      sku,
      createdAt: new Date().toISOString(),
    };

    await kv.set(`product:${productId}`, product);

    return c.json({ success: true, product });
  } catch (error) {
    console.log(`Error creating product: ${error}`);
    return c.json({ error: "Failed to create product" }, 500);
  }
});

app.get("/make-server-d8f234cd/products", async (c) => {
  try {
    const city = c.req.query("city");
    const category = c.req.query("category");
    const search = c.req.query("search");
    const businessId = c.req.query("businessId");

    const products = await kv.getByPrefix("product:");
    let filteredProducts = products;

    if (businessId) {
      filteredProducts = filteredProducts.filter(
        (p) => p.businessId === businessId,
      );
    }

    if (category && category !== "all") {
      filteredProducts = filteredProducts.filter(
        (p) =>
          p.category.toLowerCase() === category.toLowerCase(),
      );
    }

    if (search) {
      const searchLower = search.toLowerCase();
      filteredProducts = filteredProducts.filter(
        (p) =>
          p.name.toLowerCase().includes(searchLower) ||
          p.description.toLowerCase().includes(searchLower),
      );
    }

    // If city filter is applied, get businesses in that city first
    if (city && city !== "all") {
      const businesses = await kv.getByPrefix("business:");
      const cityBusinessIds = businesses
        .filter(
          (b) => b.city.toLowerCase() === city.toLowerCase(),
        )
        .map((b) => b.id);

      filteredProducts = filteredProducts.filter((p) =>
        cityBusinessIds.includes(p.businessId),
      );
    }

    // Enrich products with business information
    const enrichedProducts = [];
    for (const product of filteredProducts) {
      const business = await kv.get(
        `business:${product.businessId}`,
      );
      enrichedProducts.push({
        ...product,
        business: business || null,
      });
    }

    return c.json({ products: enrichedProducts });
  } catch (error) {
    console.log(`Error fetching products: ${error}`);
    return c.json({ error: "Failed to fetch products" }, 500);
  }
});

// Order Management Routes
app.post("/make-server-d8f234cd/orders", async (c) => {
  try {
    const {
      customerEmail,
      customerName,
      customerPhone,
      items,
      deliveryAddress,
      deliveryCity,
      totalAmount,
    } = await c.req.json();

    const orderId = crypto.randomUUID();
    const order = {
      id: orderId,
      customerEmail,
      customerName,
      customerPhone,
      items,
      deliveryAddress,
      deliveryCity,
      totalAmount: parseFloat(totalAmount),
      status: "pending",
      createdAt: new Date().toISOString(),
    };

    await kv.set(`order:${orderId}`, order);

    // Update product stock
    for (const item of items) {
      const product = await kv.get(`product:${item.productId}`);
      if (product) {
        product.stock = Math.max(
          0,
          product.stock - item.quantity,
        );
        await kv.set(`product:${item.productId}`, product);
      }
    }

    return c.json({ success: true, order });
  } catch (error) {
    console.log(`Error creating order: ${error}`);
    return c.json({ error: "Failed to create order" }, 500);
  }
});

app.get("/make-server-d8f234cd/orders/:orderId", async (c) => {
  try {
    const orderId = c.req.param("orderId");
    const order = await kv.get(`order:${orderId}`);

    if (!order) {
      return c.json({ error: "Order not found" }, 404);
    }

    return c.json({ order });
  } catch (error) {
    console.log(`Error fetching order: ${error}`);
    return c.json({ error: "Failed to fetch order" }, 500);
  }
});

app.patch(
  "/make-server-d8f234cd/orders/:orderId/status",
  async (c) => {
    try {
      const orderId = c.req.param("orderId");
      const { status } = await c.req.json();

      const order = await kv.get(`order:${orderId}`);
      if (!order) {
        return c.json({ error: "Order not found" }, 404);
      }

      order.status = status;
      order.updatedAt = new Date().toISOString();

      await kv.set(`order:${orderId}`, order);

      return c.json({ success: true, order });
    } catch (error) {
      console.log(`Error updating order status: ${error}`);
      return c.json(
        { error: "Failed to update order status" },
        500,
      );
    }
  },
);

// User Authentication Routes
app.post("/make-server-d8f234cd/auth/signup", async (c) => {
  try {
    const { email, password, name, userType } =
      await c.req.json();

    console.log("Signup attempt for:", email);
    console.log("Supabase URL configured:", !!supabaseUrl);

    const { data, error } =
      await supabase.auth.admin.createUser({
        email,
        password,
        user_metadata: { name, userType },
        // Automatically confirm the user's email since an email server hasn't been configured.
        email_confirm: true,
      });

    if (error) {
      console.log(
        `Error creating user during signup:`, error
      );
      return c.json({ error: error.message }, 400);
    }

    return c.json({ success: true, user: data.user });
  } catch (error) {
    console.log(`Error during user signup:`, error);
    return c.json({ error: "Failed to create user" }, 500);
  }
});

// Categories and Cities
app.get("/make-server-d8f234cd/categories", async (c) => {
  try {
    const categories = [
      "Electronics",
      "Fashion & Clothing",
      "Home & Garden",
      "Sports & Outdoors",
      "Books & Media",
      "Health & Beauty",
      "Automotive",
      "Food & Beverages",
      "Art & Crafts",
      "Services",
    ];

    return c.json({ categories });
  } catch (error) {
    console.log(`Error fetching categories: ${error}`);
    return c.json({ error: "Failed to fetch categories" }, 500);
  }
});

app.get("/make-server-d8f234cd/cities", async (c) => {
  try {
    const cities = [
      "Harare",
      "Bulawayo",
      "Chitungwiza",
      "Mutare",
      "Epworth",
      "Gweru",
      "Kwekwe",
      "Kadoma",
      "Masvingo",
      "Chinhoyi",
      "Norton",
      "Marondera",
      "Ruwa",
      "Chegutu",
      "Zvishavane",
    ];

    return c.json({ cities });
  } catch (error) {
    console.log(`Error fetching cities: ${error}`);
    return c.json({ error: "Failed to fetch cities" }, 500);
  }
});

Deno.serve(app.fetch);

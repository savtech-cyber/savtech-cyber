// kv_store.ts - Simple KV store using Deno KV
const kv = await Deno.openKv();

export async function set(key: string, value: any): Promise<void> {
  await kv.set([key], value);
}

export async function get(key: string): Promise<any> {
  const result = await kv.get([key]);
  return result.value;
}

export async function getByPrefix(prefix: string): Promise<any[]> {
  const results: any[] = [];
  const iter = kv.list({ prefix: [prefix] });
  
  for await (const entry of iter) {
    results.push(entry.value);
  }
  
  return results;
}

export async function delete_(key: string): Promise<void> {
  await kv.delete([key]);
}

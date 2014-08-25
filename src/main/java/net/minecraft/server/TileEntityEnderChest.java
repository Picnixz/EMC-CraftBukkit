package net.minecraft.server;

public class TileEntityEnderChest extends TileEntity {

    public float a;
    public float i;
    public int j;
    private int k;

    public TileEntityEnderChest() {}

    public void h() {
        super.h();
        if (true) { // Spigot Rate controlled by Improved Tick Handling
            this.world.playNote(this.x, this.y, this.z, Blocks.ENDER_CHEST, 1, this.j);
        }
    } // Spigot
    private void sendSounds(boolean isOpen) { // Spigot
        this.i = this.a;
        float f = 0.1F;
        double d0;

        if (isOpen && this.j == 1) { // Spigot - send only on open when == 1
            double d1 = (double) this.x + 0.5D;

            d0 = (double) this.z + 0.5D;
            this.world.makeSound(d1, (double) this.y + 0.5D, d0, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (!isOpen && this.j == 0) { // Spigot - Send on close when == 0
            float f1 = this.a;

            if (this.j > 0) {
                this.a += f;
            } else {
                this.a -= f;
            }

            if (this.a > 1.0F) {
                this.a = 1.0F;
            }

            float f2 = 0.5F;

            if (true || this.a < f2 && f1 >= f2) { // Spigot - Always send
                d0 = (double) this.x + 0.5D;
                double d2 = (double) this.z + 0.5D;

                this.world.makeSound(d0, (double) this.y + 0.5D, d2, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.a < 0.0F) {
                this.a = 0.0F;
            }
        }
    }

    public boolean c(int i, int j) {
        if (i == 1) {
            this.j = j;
            return true;
        } else {
            return super.c(i, j);
        }
    }

    public void s() {
        this.u();
        super.s();
    }

    public void a() {
        ++this.j;
        sendSounds(true); // Spigot
        this.world.playNote(this.x, this.y, this.z, Blocks.ENDER_CHEST, 1, this.j);
    }

    public void b() {
        --this.j;
        sendSounds(false); // Spigot
        this.world.playNote(this.x, this.y, this.z, Blocks.ENDER_CHEST, 1, this.j);
    }

    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }
}

package com.imageinnova.transporter.blocks;

import com.imageinnova.transporter.Transporter;
import com.imageinnova.transporter.network.TransporterGuiHandler;
import com.imageinnova.transporter.tileentities.TileEntityTransporter;
import com.imageinnova.transporter.worldsaveddata.TransporterList;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockTransporter extends Block implements ITileEntityProvider {
	public BlockTransporter(String unlocalizedName) {
		super(Material.IRON);
		this.setRegistryName(Transporter.MODID, unlocalizedName);
		this.setUnlocalizedName(unlocalizedName);
		setCreativeTab(CreativeTabs.REDSTONE);
		this.setHardness(2.0f);
		this.setResistance(6.0f);
		this.setHarvestLevel("pickaxe", 2);
		this.setLightLevel(0.5f); 
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityTransporter();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
	    TileEntityTransporter te = (TileEntityTransporter) world.getTileEntity(pos);
	    IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	    ItemStack stack = itemHandler.getStackInSlot(te.INPUT_SLOT);
	    if (stack != null) {
	    	EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
	    	world.spawnEntity(item);
	    }
	    super.breakBlock(world, pos, blockstate);
	    if (!world.isRemote) {
	    	TransporterList.get(world).remove(pos);
	    }
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
	    if (stack.hasDisplayName()) {
	        ((TileEntityTransporter) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
	    }
	    if (!worldIn.isRemote) {
	    	TransporterList.get(worldIn).add(pos);
	    }
	}
	
	@Override 
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
	    if (worldIn.isRemote) {
	    	return true;
	    }

	    playerIn.openGui(Transporter.instance, TransporterGuiHandler.TRANSPORTER_ENTITY_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
	    return true;
	}
}

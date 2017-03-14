package com.imageinnova.transporter.blocks;

import com.imageinnova.transporter.Transporter;
import com.imageinnova.transporter.network.ModGuiHandler;
import com.imageinnova.transporter.tileentities.TileEntityTransporter;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockTransporter extends BlockContainer {
	protected BlockTransporter(String unlocalizedName) {
		super(Material.iron);
		this.setUnlocalizedName(unlocalizedName);
		setCreativeTab(CreativeTabs.tabRedstone);
		this.setHardness(2.0f);
		this.setResistance(6.0f);
		this.setHarvestLevel("pickaxe", 2);
		this.setLightLevel(0.5f); 
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityTransporter();
	}
	
	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
	    TileEntityTransporter te = (TileEntityTransporter) world.getTileEntity(pos);
	    InventoryHelper.dropInventoryItems(world, pos, te);
	    super.breakBlock(world, pos, blockstate);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
	    if (stack.hasDisplayName()) {
	        ((TileEntityTransporter) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
	    }
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
	    if (!world.isRemote) {
	        player.openGui(Transporter.instance, ModGuiHandler.TRANSPORTER_ENTITY_GUI, world, pos.getX(), pos.getY(), pos.getZ());
	    }
	    return true;
	}
}

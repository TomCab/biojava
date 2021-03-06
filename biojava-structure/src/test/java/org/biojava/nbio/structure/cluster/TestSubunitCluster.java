/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */
package org.biojava.nbio.structure.cluster;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.structure.AminoAcidImpl;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.AtomImpl;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureTools;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.junit.Test;

/**
 * Test the {@link SubunitCluster} merge and divide methods, one test specific
 * for each method.
 * 
 * @author Aleix Lafita
 *
 */
public class TestSubunitCluster {

	/**
	 * Test {@link SubunitCluster#mergeIdentical(SubunitCluster)}.
	 */
	@Test
	public void testMergeIdentical() {

		// Create an Atom Array of ploy-alanine
		List<Atom> atoms = new ArrayList<Atom>(10);
		for (int i = 0; i < 10; i++) {
			Group g = new AminoAcidImpl();
			g.setPDBName("ALA");
			Atom a = new AtomImpl();
			a.setName(StructureTools.CA_ATOM_NAME);
			g.addAtom(a);
			atoms.add(a);
		}
		Atom[] reprAtoms = atoms.toArray(new Atom[atoms.size()]);

		// Create two identical SubunitCluster
		SubunitCluster sc1 = new SubunitCluster(new Subunit(reprAtoms));
		SubunitCluster sc2 = new SubunitCluster(new Subunit(reprAtoms));

		boolean merged = sc1.mergeIdentical(sc2);

		// Merged have to be true, and the merged SubunitCluster is sc1
		assertTrue(merged);
		assertEquals(sc1.size(), 2);
		assertEquals(sc2.size(), 1);
		assertEquals(sc1.length(), 10);

		// Create an Atom Array of poly-glycine
		List<Atom> atoms2 = new ArrayList<Atom>(10);
		for (int i = 0; i < 10; i++) {
			Group g = new AminoAcidImpl();
			g.setPDBName("GLY");
			Atom a = new AtomImpl();
			a.setName(StructureTools.CA_ATOM_NAME);
			g.addAtom(a);
			atoms2.add(a);
		}
		Atom[] reprAtoms2 = atoms2.toArray(new Atom[atoms2.size()]);

		SubunitCluster sc3 = new SubunitCluster(new Subunit(reprAtoms2));

		merged = sc1.mergeIdentical(sc3);

		// Merged have to be false, and Clusters result inmodified
		assertFalse(merged);
		assertEquals(sc1.size(), 2);
		assertEquals(sc2.size(), 1);
		assertEquals(sc1.length(), 10);

	}

	/**
	 * Test {@link SubunitCluster#mergeSequence(SubunitCluster, double, double)}
	 * 
	 * @throws CompoundNotFoundException
	 */
	@Test
	public void testMergeSequence() throws CompoundNotFoundException {

		// Create an Atom Array of ploy-alanine
		List<Atom> atoms = new ArrayList<Atom>(100);
		for (int i = 0; i < 100; i++) {
			Group g = new AminoAcidImpl();
			g.setPDBName("ALA");
			Atom a = new AtomImpl();
			a.setName(StructureTools.CA_ATOM_NAME);
			g.addAtom(a);
			atoms.add(a);
		}
		Atom[] reprAtoms = atoms.toArray(new Atom[atoms.size()]);

		// Create two identical SubunitCluster
		SubunitCluster sc1 = new SubunitCluster(new Subunit(reprAtoms));
		SubunitCluster sc2 = new SubunitCluster(new Subunit(reprAtoms));

		boolean merged = sc1.mergeSequence(sc2, 0.9, 0.9);

		// Merged have to be true, and the merged SubunitCluster is sc1
		assertTrue(merged);
		assertEquals(sc1.size(), 2);
		assertEquals(sc2.size(), 1);
		assertEquals(sc1.length(), 100);

		// Create an Atom Array of poly-glycine
		List<Atom> atoms2 = new ArrayList<Atom>(100);
		for (int i = 0; i < 100; i++) {
			Group g = new AminoAcidImpl();
			g.setPDBName("GLY");
			Atom a = new AtomImpl();
			a.setName(StructureTools.CA_ATOM_NAME);
			g.addAtom(a);
			atoms2.add(a);
		}
		Atom[] reprAtoms2 = atoms2.toArray(new Atom[atoms2.size()]);

		SubunitCluster sc3 = new SubunitCluster(new Subunit(reprAtoms2));

		merged = sc1.mergeSequence(sc3, 0.9, 0.9);

		// Merged have to be false, and Clusters result inmodified
		assertFalse(merged);
		assertEquals(sc1.size(), 2);
		assertEquals(sc2.size(), 1);
		assertEquals(sc1.length(), 100);

		// Create an Atom Array of 9 glycine and 91 alanine
		List<Atom> atoms3 = new ArrayList<Atom>(100);
		for (int i = 0; i < 9; i++) {
			Group g = new AminoAcidImpl();
			g.setPDBName("GLY");
			Atom a = new AtomImpl();
			a.setName(StructureTools.CA_ATOM_NAME);
			g.addAtom(a);
			atoms3.add(a);
		}
		for (int i = 0; i < 91; i++) {
			Group g = new AminoAcidImpl();
			g.setPDBName("ALA");
			Atom a = new AtomImpl();
			a.setName(StructureTools.CA_ATOM_NAME);
			g.addAtom(a);
			atoms3.add(a);
		}
		Atom[] reprAtoms3 = atoms3.toArray(new Atom[atoms3.size()]);

		SubunitCluster sc4 = new SubunitCluster(new Subunit(reprAtoms3));

		merged = sc1.mergeSequence(sc4, 0.9, 0.9);

		// Merged have to be true, and the merged SubunitCluster is sc1
		assertTrue(merged);
		assertEquals(sc1.size(), 3);
		assertEquals(sc2.size(), 1);
		assertEquals(sc1.length(), 91);

	}

	/**
	 * Test
	 * {@link SubunitCluster#mergeStructure(SubunitCluster, double, double)}
	 * 
	 * @throws StructureException
	 * @throws IOException
	 */
	@Test
	public void testMergeStructure() throws StructureException, IOException {

		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		Structure s = cache.getStructure("4hhb");

		// Create one SubunitCluster for each chain
		SubunitCluster sc1 = new SubunitCluster(
				new Subunit(StructureTools.getRepresentativeAtomArray(s
						.getChainByIndex(0))));
		SubunitCluster sc2 = new SubunitCluster(
				new Subunit(StructureTools.getRepresentativeAtomArray(s
						.getChainByIndex(1))));
		SubunitCluster sc3 = new SubunitCluster(
				new Subunit(StructureTools.getRepresentativeAtomArray(s
						.getChainByIndex(2))));
		SubunitCluster sc4 = new SubunitCluster(
				new Subunit(StructureTools.getRepresentativeAtomArray(s
						.getChainByIndex(3))));

		// Clusters 1 and 3 and 2 and 4 are identical
		boolean merged13 = sc1.mergeStructure(sc3, 3.0, 0.9);
		boolean merged24 = sc2.mergeStructure(sc4, 3.0, 0.9);

		// Merged have to be true, and the merged SubunitCluster is sc1
		assertTrue(merged13);
		assertTrue(merged24);
		assertEquals(sc1.size(), 2);
		assertEquals(sc2.size(), 2);
		assertEquals(sc1.length(), 141);
		assertEquals(sc2.length(), 146);
		assertEquals(sc1.getAlignedAtomsSubunit(0).length,
				sc1.getAlignedAtomsSubunit(1).length);
		assertEquals(sc2.getAlignedAtomsSubunit(0).length,
				sc2.getAlignedAtomsSubunit(1).length);

		// Now test for pseudosymmetry
		boolean merged = sc1.mergeStructure(sc2, 3.0, 0.9);

		assertTrue(merged);
		assertEquals(sc1.size(), 4);
		assertEquals(sc1.length(), 140, 2);
		assertEquals(sc1.getAlignedAtomsSubunit(0).length,
				sc1.getAlignedAtomsSubunit(2).length);
		

	}

	/**
	 * Test {@link SubunitCluster#divideInternally(double, double, int)}
	 * 
	 * @throws StructureException
	 * @throws IOException
	 */
	@Test
	public void testDivideInternally() throws StructureException, IOException {

		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		Structure s = cache.getStructure("4e3e");

		// Create a SubunitCluster for the chain
		SubunitCluster sc1 = new SubunitCluster(
				new Subunit(StructureTools.getRepresentativeAtomArray(s
						.getChainByIndex(0))));

		// Clusters should be merged by identity
		boolean divided = sc1.divideInternally(0.8, 3.0, 20);

		// Divided has to be true, and Subunit length shorter than half
		assertTrue(divided);
		assertEquals(sc1.size(), 2);
		assertTrue(sc1.length() < 178);
		assertEquals(sc1.getAlignedAtomsSubunit(0).length,
				sc1.getAlignedAtomsSubunit(1).length);
	}
}

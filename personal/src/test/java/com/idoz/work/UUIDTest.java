package com.idoz.work;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by izilberberg on 9/29/14.
 */

public class UUIDTest
{

  @Test
  public void testUUIDFile() throws IOException {

    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 1.0f, 2);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 0.2f, 2);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 0.1f, 2);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 0.07f, 2);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 1.0f, 4);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 0.2f, 4);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 0.1f, 4);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 0.07f, 4);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 1.0f, 5);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 0.2f, 5);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 0.1f, 5);
    runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"), 0.07f, 5);

//  runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_abtest935.txt").getFile()), "UTF-8"), 1.0f, 2);

    //runUuidTest(FileUtils.readLines(new File(getClass().getResource("/uuids_abtest882.txt").getFile()), "UTF-8"), 0.07f, 5);

//    testCalcUuidDistribution(FileUtils.readLines(new File(getClass().getResource("/uuids_100k.txt").getFile()), "UTF-8"));

  }



  private void runUuidTest(List<String> lines, float percentage, int variantSize)  {


    int[] variantsOrig = new int[variantSize+1];
    int[] variantsNew = new int[variantSize+1];

    System.out.println("Test [Original UUID Calculation]: traffic pct: " + percentage + ", variants: " + variantSize);


    long start = System.currentTimeMillis();
    for(String line: lines)   {
      if( line.length()==0 ) continue;
      float uuid = uuidToFloatOriginal(line);
      int variant = calcVariantIndex(uuid, percentage, variantSize);
      //System.out.println("uuid: " + uuid + " " + variant);
      variantsOrig[variant]++;
    }
    System.out.println("Time: " + (System.currentTimeMillis()-start) + " ms");
    System.out.println("Not in AB Test: " + variantsOrig[0] + "(" + variantsOrig[0]/1000.0f + "%)");
    for(int i=1; i<=variantSize; i++)  {
      System.out.println("Variant " + i + ": " + variantsOrig[i] + "(" + variantsOrig[i]/1000.0f + "%)");
    }

    System.out.println("Test [New UUID Calculation]: traffic pct: " + percentage + ", variants: " + variantSize);

    start = System.currentTimeMillis();
    for(String line: lines)   {
      if( line.length()==0 ) continue;
      float uuid = uuidToFloatNew(line);
      int variant = calcVariantIndex(uuid, percentage, variantSize);
      //System.out.println("uuid: " + uuid + " " + variant);
      variantsNew[variant]++;
    }

    System.out.println("Time: " + (System.currentTimeMillis()-start) + " ms");
    System.out.println("Not in AB Test: " + variantsNew[0] + "(" + variantsNew[0]/1000.0f + "%)");
    for(int i=1; i<=variantSize; i++)  {
      System.out.println("Variant " + i + ": " + variantsNew[i] + "(" + variantsNew[i]/1000.0f + "%)");
    }


  }

  public void testCalcUuidDistribution(List<String> lines)  {

    System.out.println("==============");

    int[] buckets = new int[65536];
    for(String line: lines)   {
      if( line.length()==0 ) continue;
      buckets[(int)(uuidToFloatNew(line)*65536.0)]++;
    }

    int[] bucketsize = new int[20];


    for(int i=0; i<65536; i++)  {
      bucketsize[buckets[i]]++;
      if( buckets[i]>9 )
        System.out.println(i + ": " + buckets[i]);
    }
    for(int j=0; j<20; j++) {
      System.out.println(j + ": " + bucketsize[j]);
    }


  }

  private int calcIntFromUUID(final String uuid)    {
    final int UUID_GRANULARITY = 2; // this is the # of hexadecimal digits from the uuid we shall take into account when calculating the float.
    final int UUID_RADIX = 16; // this is the numerical base of the UUID
    final long MAX_UUID = (long) Math.pow(UUID_RADIX, UUID_GRANULARITY);
    String suffix = uuid.replace("-", "");
    suffix = StringUtils.right(suffix, UUID_GRANULARITY);
    int num = Integer.parseInt(suffix, UUID_RADIX);
    return num;

  }


  private int calcVariantIndex(final float uuidToFloat, final float percentage, final int variantSize) {

    final float numerator = uuidToFloat - (1 - percentage);
    if( numerator < 0 )   {
      return 0; // not in AB test
    }
    final float denominator = percentage;
    float result = (numerator / denominator) * variantSize;
    int intResult = (int)result;
    return intResult+1;
  }

  public static float uuidToFloatOriginal(final String uuid) {

    final int UUID_GRANULARITY = 2; // this is the # of hexadecimal digits from the uuid we shall take into account when calculating the float.
    final int UUID_RADIX = 16; // this is the numerical base of the UUID
    final long MAX_UUID = (long) Math.pow(UUID_RADIX, UUID_GRANULARITY);
    String suffix = uuid.replace("-", "");
    suffix = StringUtils.right(suffix, UUID_GRANULARITY);
    final int currUuidSuffixAsNumber = Integer.parseInt(suffix, UUID_RADIX);


    return (float) currUuidSuffixAsNumber / (float) MAX_UUID;
  }


//  @Test
//  public void verifyOldAlgo() {
//    System.out.println("00:" + uuidToFloatOriginal("00"));
//    System.out.println("a0:" + uuidToFloatOriginal("a0"));
//    System.out.println("ff:" + uuidToFloatOriginal("ff"));
//  }
//
//
//  @Test
//  public void verifyNewAlgo() {
//    System.out.println("0000:" + uuidToFloatNew("0000"));
//    System.out.println("a000:" + uuidToFloatNew("a000"));
//    System.out.println("ffff:" + uuidToFloatNew("ffff"));
//  }


  public static float uuidToFloatNew(final String uuid) {

    final int UUID_GRANULARITY = 4; // this is the # of hexadecimal digits from the uuid we shall take into account when calculating the float.
    final int UUID_RADIX = 16; // this is the numerical base of the UUID
    final long MAX_UUID = (long) Math.pow(UUID_RADIX, UUID_GRANULARITY);
    String suffix = uuid.replace("-", "");
    suffix = StringUtils.right(suffix, UUID_GRANULARITY);
    final int currUuidSuffixAsNumber = Integer.parseInt(suffix, UUID_RADIX);
    return (float) currUuidSuffixAsNumber / (float) MAX_UUID;
  }



}
